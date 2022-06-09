import ImageRender from './canvas/Canvas2.js';
import DrawCanvas from './canvas/DrawCanvas.js';
import FileHelper from './helper/FileHelper.js';
import ImageHelper from './helper/ImageHelper.js';
import State from './model/State.js';
import Magnifier from './Magnifier.js';
import StagesPanel from './StagesPanel.js';
import ParsingAPI from './helper/ParsingAPI.js';

const IMAGE_DATA_PREFIX = 'data:image/png;base64,';

class App {
    constructor(props) {
        this.canvas = null;
        this.state = new State();
        let imageCanvas = document.getElementById('image-render');
        this.magnifier = this.magnifier = new Magnifier(document.getElementById('magnifier'), document.getElementById('coord-x'), document.getElementById('coord-y'));
        this.drawCanvas = new DrawCanvas(document.getElementById('draw-render'), imageCanvas, this.state, this.magnifier);
        this.imageRender = new ImageRender(imageCanvas, document.getElementById('canvas-wrapper'), this.drawCanvas, this.state);
        this.applicationContext = {};

        this.stagesPanel = new StagesPanel();
        this.stagesPanel.onNextStage.add((e) => this.nextStageHandler(e));
        this.stagesPanel.onPrevStage.add((e) => this.prevStageHandler(e));

        $('#btn-prev').click(() => this.stagesPanel.prevStage());
        $('#btn-next').click(() => this.stagesPanel.nextStage());
        this.initFileDropper();

        const self = this;
        $('#rotation-range').on('input', (e) => {
            $('#rotation-text').val(e.currentTarget.value);
            const istate = self.state.transform;
            istate.rotation = parseInt(e.currentTarget.value);
            self.imageRender.renderFraming();
        });

        $('#rotation-text').on('input', (e) => {
            $('#rotation-range').val(e.currentTarget.value);
            const istate = self.state.transform;
            istate.rotation = parseInt(e.currentTarget.value);
            self.imageRender.renderFraming();
        });

        $('#flip-horizontal').click((e) => {
            $(e.currentTarget).toggleClass('item-active');
            const istate = self.state.transform;
            istate.flipX = !istate.flipX;
            self.imageRender.renderFraming();
        });

        $('#flip-vertical').click((e) => {
            $(e.currentTarget).toggleClass('item-active');
            const istate = self.state.transform;
            istate.flipY = !istate.flipY;
            self.imageRender.renderFraming();
        });

        $.ajaxSetup({
            contentType: 'application/json; charset=UTF-8'
        });

        this.initBtns();

        console.log('app initialized');
    }

    initFileDropper() {
        let dragArea = document.getElementById('drop-area');
        dragArea.ondragenter = function (e) {e.preventDefault()};
        dragArea.ondragover = function (e) {e.preventDefault()};
        dragArea.ondrop = function (e) {dropFileHandler(e)};
        let fileInput = document.getElementById('file-input');
        fileInput.onchange = function (e) {inputFileHandler(e)};
        let app = this;

        function dropFileHandler(event) {
            event.preventDefault();
            const file = event.originalEvent.dataTransfer.files[0];
            if (file !== undefined) {
                onFileLoad(file);
            }
        }
        function inputFileHandler(event) {
            const file = event.currentTarget.files[0];
            if (file !== undefined) {
                onFileLoad(file);
                fileInput.value = null;
            }
        }
        function onFileLoad(file) {
            FileHelper.fileToBase64Async(file).then((base64) => {
                app.state.image = base64;
                app.stagesPanel.nextStage();
            });
        }
    }

    nextStageHandler(event) {
        const stageId = event.id;
        console.log(stageId);
        if (stageId === 1) {
            this.changeToolbar('tool-group-0', true);
            $('#drop-area').addClass('hidden');
            $('#canvas-wrapper').removeClass('hidden');
            $('#sidebar').removeClass('hidden');
            this.imageRender.setImage(this.state.image);
            this.drawCanvas.initDrawRectangleSelect();
        } else if (stageId === 2) {
            this.changeToolbar('tool-group-1', true);
            this.drawCanvas.initDisableDraw();
        } else if (stageId === 3) {
            this.changeToolbar('tool-group-2', true);
            document.querySelector('#tool-group-2 .item-btn.item-active').click();
        } else if (stageId === 4) {
            this.changeToolbar('tool-group-3', true);
            this.drawCanvas.initDrawAxes();
        } else if (stageId === 5) {
            this.changeToolbar('tool-group-4', true);
            this.drawCanvas.initDrawPoints();
        } else if (stageId === 6) {
            this.changeToolbar('tool-group-5', false);
        }
    }

    prevStageHandler(event) {
        const stageId = event.id;

        if (stageId === 0) {
            $('#drop-area').removeClass('hidden');
            $('#canvas-wrapper').addClass('hidden');
            $('#sidebar').addClass('hidden');
        }

        if (stageId === 1) {
            this.canvas.restoreImage();
            this.canvas.render();
        }
    }

    initBtns() {
        // stage 1
        let imgEditBtn = document.getElementById('img-edit-btn');
        let self = this;
        imgEditBtn.onclick = function () {
            self.state.transform.rotation = 0;
            self.state.transform.flipX = false;
            self.state.transform.flipY = false;
            let points = self.drawCanvas.convertPointsToImageCanvasSize(self.state.transform.rectangle);
            let cutOptions = ImageHelper.prepareRectanglePoints(points);
            self.state.image = ImageHelper.cutImage(self.imageRender.canvas, cutOptions.x, cutOptions.y, cutOptions.w, cutOptions.h);
            self.imageRender.setImage(self.state.image);
            self.updateFramingView();
        }
        document.getElementById('perspective-btn').onclick = function () {
            let points = self.drawCanvas.convertPointsToImageCanvasSize(self.state.transform.perspective);
            let prepared = [points[0], points[3], points[2], points[1]];
            ImageHelper.changePerspective(self.imageRender.canvas.toDataURL(), prepared,function (result) {
                self.state.image = IMAGE_DATA_PREFIX + result.image;
                self.imageRender.setImage(self.state.image);
                self.state.setDefaultTransform();
            });
        }
        this.togglePanel(document.getElementById('tool-group-0-btn-0'), 'tool-group-0-subs', 'tool-group-0-0', '#tool-group-0 .tabs', function (){
            self.drawCanvas.initDrawRectangleSelect();
            self.state.setDefaultTransform();
            self.imageRender.renderFraming();
        });
        this.togglePanel(document.getElementById('tool-group-0-btn-1'), 'tool-group-0-subs', 'tool-group-0-1', '#tool-group-0 .tabs', function (){
            self.drawCanvas.initDrawPolygonSelect();
            self.state.setDefaultTransform();
            self.imageRender.renderFraming();
        });
        // stage 2
        let contrast = document.getElementById('contrast-range');
        contrast.value = this.state.colors.contrast;
        contrast.oninput = function (e) {
            self.state.setContrast(parseInt(contrast.value));
            self.imageRender.renderColors();
        }
        let brightness = document.getElementById('brightness-range');
        brightness.value = this.state.colors.contrast;
        brightness.oninput = function (e) {
            self.state.setBrightness(parseInt(brightness.value));
            self.imageRender.renderColors();
        }

        let saturation = document.getElementById('saturation-range');
        saturation.value = this.state.colors.contrast;
        saturation.oninput = function (e) {
            self.state.setSaturation(parseInt(saturation.value));
            self.imageRender.renderColors();
        }
        let invert = document.getElementById('invert-cb');
        invert.onchange = function () {
            self.state.setInvert(invert.checked);
            self.imageRender.renderColors();
        }
        // stage 3
        let areaSelectBtnsParent = document.querySelector('#tool-group-2 .tabs');
        this.toggleAreaSelectBtns(document.getElementById('add-rect-btn'), areaSelectBtnsParent, function () {
            self.drawCanvas.initDrawRectangles(true);
        });
        this.toggleAreaSelectBtns(document.getElementById('delete-rect-btn'), areaSelectBtnsParent, function () {
            self.drawCanvas.initDrawRectangles(false);
        });
        this.toggleAreaSelectBtns(document.getElementById('pencil-btn'), areaSelectBtnsParent, function () {
            self.drawCanvas.initDrawPencil(true);
        });
        this.toggleAreaSelectBtns(document.getElementById('eraser-btn'), areaSelectBtnsParent, function () {
            self.drawCanvas.initDrawPencil(false);
        });
        let pencilRange = document.getElementById('pencil-range');
        pencilRange.oninput = function () {
            self.state.setPencil(parseInt(pencilRange.value));
        }
        let eraserRange = document.getElementById('eraser-range');
        eraserRange.oninput = function () {
            self.state.setEraser(parseInt(eraserRange.value));
        }
        document.getElementById('temp').onclick = function () {
            let canv = ImageHelper.cutByImage(self.imageRender.canvas, self.drawCanvas.canvas);
            let base64Image = canv.toDataURL();
            ParsingAPI.sendRequest('/api/image',
                JSON.stringify({
                    image: FileHelper.parseDataURL(base64Image).data
                })
            ).then((result) => {
                    self.state.setResultImage({base64: base64Image, id: result.imageId});
                    console.log(result);
                });
            // var dataURL = canv.toDataURL("image/png");
            // var newTab = window.open('about:blank','image from canvas');
            // newTab.document.write("<img src='" + dataURL + "' alt='from canvas'/>");
        }
        // stage 4
        let xAxisSelect = document.getElementById('X-axis-type');
        xAxisSelect.value = this.state.axes.x.type;
        xAxisSelect.oninput = function () {
            self.state.setXAxisType(xAxisSelect.value);
        }
        let xAxisStart = document.getElementById('x-start');
        xAxisStart.value = this.state.axes.x.value[0];
        xAxisStart.oninput = function () {
            self.state.setXAxisStart(parseFloat(xAxisStart.value));
        }
        let xAxisEnd = document.getElementById('x-end');
        xAxisEnd.value = this.state.axes.x.value[1];
        xAxisEnd.oninput = function () {
            self.state.setXAxisEnd(parseFloat(xAxisEnd.value));
        }
        let yAxisSelect = document.getElementById('y-axis-type');
        yAxisSelect.value = this.state.axes.y.type;
        yAxisSelect.oninput = function () {
            self.state.setYAxisType(yAxisSelect.value);
        }
        let yAxisStart = document.getElementById('y-start');
        yAxisStart.value = this.state.axes.y.value[0];
        yAxisStart.oninput = function () {
            self.state.setYAxisStart(parseFloat(yAxisStart.value));
        }
        let yAxisEnd = document.getElementById('y-end');
        yAxisEnd.value = this.state.axes.y.value[1];
        yAxisEnd.oninput = function () {
            self.state.setYAxisEnd(parseFloat(yAxisEnd.value));
        }


    }

    togglePanel(button, subsId, enableGroupId, queryForTabs, action) {
        button.onclick = function () {
            if (!button.classList.contains('item-active')) {
                let tabs = document.querySelector(queryForTabs).children;
                for (let i = 0; i < tabs.length; i++) {
                    tabs[i].classList.remove('item-active');
                }
                button.classList.add('item-active');
                let subs = document.getElementById(subsId).children;
                for (let i = 0; i < subs.length; i++) {
                    subs[i].classList.add('d-none');
                }
                document.getElementById(enableGroupId).classList.remove('d-none');
                action();
            }
        }
    }

    toggleAreaSelectBtns(btn, btnsParent, action) {
        btn.onclick = function () {
            if (!btn.classList.contains('item-active')) {
                for (let i = 0; i < btnsParent.children.length; i++) {
                    let btn = btnsParent.children[i];
                    btn.classList.remove('item-active');
                }
                btn.classList.add('item-active');
                action();
            }
        }
    }

    changeToolbar(toolId, showMagnifier) {
        let tools = document.querySelector('.toolbar').children;
        for (let i = 0; i < tools.length; i++) {
            tools[i].classList.add('d-none');
        }
        document.getElementById(toolId).classList.remove('d-none');
        if (showMagnifier) {
            document.getElementById('magnifier-container').classList.remove('d-none');
            document.querySelector('#sidebar .tool-scrollable').classList.remove('show-table');
        } else {
            document.getElementById('magnifier-container').classList.add('d-none');
            document.querySelector('#sidebar .tool-scrollable').classList.add('show-table');
        }
    }

    updateFramingView() {
        document.getElementById('rotation-text').value = this.state.transform.rotation;
        document.getElementById('rotation-range').value = this.state.transform.rotation;
        this.changeFlipView(document.getElementById('flip-vertical'), this.state.transform.rotation.flipY)
        this.changeFlipView(document.getElementById('flip-horizontal'), this.state.transform.rotation.flipX)
    }

    changeFlipView(flip, isFlip) {
        if (isFlip) {
            flip.classList.add('item-active');
        } else {
            flip.classList.remove('item-active');
        }
    }
}

$(document).ready(new App());

export default App;
