import Canvas from './Canvas.js';
import ImageRender from './canvas/Canvas2.js';
import DrawCanvas from './canvas/DrawCanvas.js';
import FileHelper from './helper/FileHelper.js';
import ImageHelper from './helper/ImageHelper.js';
import State from './model/State.js';
import Magnifier from './Magnifier.js';
import ParsingAPI from './helper/ParsingAPI.js';
import StagesPanel from './StagesPanel.js';
import Toolbar from './Toolbar.js';

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

        this.toolbar = new Toolbar({
            searchPrefix: 'tool-group',
            appContext: this.applicationContext
        });

        // Handle toolbar input
        const self = this;
        $('#rotation-range').on('input', (e) => {
            $('#rotation-text').val(e.currentTarget.value);
            const istate = self.state.transform;
            istate.rotation = parseInt(e.currentTarget.value);
            self.imageRender.render();
        });

        $('#rotation-text').on('input', (e) => {
            $('#rotation-range').val(e.currentTarget.value);
            const istate = self.state.transform;
            istate.rotation = parseInt(e.currentTarget.value);
            self.imageRender.render();
        });

        $('#flip-horizontal').click((e) => {
            $(e.currentTarget).toggleClass('item-active');
            const istate = self.state.transform;
            istate.flipX = !istate.flipX;
            self.imageRender.render();
        });

        $('#flip-vertical').click((e) => {
            $(e.currentTarget).toggleClass('item-active');
            const istate = self.state.transform;
            istate.flipY = !istate.flipY;
            self.imageRender.render();
        });

        $.ajaxSetup({
            contentType: 'application/json; charset=UTF-8'
        });

        $('#perspective-btn').click(() => {
            const points = self.canvas.elements[0].getPoints();
            ParsingAPI.sendRequest('/api/perspective',
                JSON.stringify({
                    points: points,
                    image: FileHelper.parseDataURL(self.canvas.getImageDataURL()).data,
                    outputWidth: Math.round((Math.abs(points[1][0] - points[0][0]) + Math.abs(points[2][0] - points[3][0])) / 2),
                    outputHeight: Math.round((Math.abs(points[3][1] - points[0][1]) + Math.abs(points[2][1] - points[1][1])) / 2)
                })
            )
            .then((result) => {
                self.image.id = result.imageId;
                self.canvas.setImageState(Canvas.defaultImageState());
                self.canvas.setImage(IMAGE_DATA_PREFIX + result.image);
            });
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
                app.state.image = {
                    file: file,
                    base64: FileHelper.parseDataURL(base64)
                }
                app.stagesPanel.nextStage();
            });
        }
    }

    initCanvasAndMagnifier() {
        if (this.canvas == null) {
            let canvasWrapperRect = document.getElementById('canvas-wrapper').getBoundingClientRect();
            this.canvas = new Canvas({
                canvas: $('#canvas').get(0),
                initWidth: parseInt(canvasWrapperRect.width),
                initHeight: parseInt(canvasWrapperRect.height),
                magnifier: this.magnifier
            });
            this.applicationContext.canvas = this.canvas;
        }
        let wrapRect = document.getElementById('canvas-wrapper').getBoundingClientRect();
        this.drawCanvas.canvas.width = wrapRect.width;
        this.drawCanvas.canvas.height = wrapRect.height;
    }

    nextStageHandler(event) {
        const stageId = event.id;

        if (stageId === 1) {
            $('#drop-area').addClass('hidden');
            $('#canvas-wrapper').removeClass('hidden');
            $('#sidebar').removeClass('hidden');

            this.initCanvasAndMagnifier();
            this.imageRender.setImage(this.state.image.base64.full);
            this.drawCanvas.initDrawRectangleSelect();
            // this.canvas.setImage(this.image.base64.full);
        }

        if (stageId === 2) {
            this.canvas.saveImage();

            if (this.toolbar.subIndex === 0) {
                const points = this.canvas.elements[0].getPoints();
                this.state.image.base64 = FileHelper.parseDataURL(this.canvas.getSubImageDataURL(
                    points[0][0],
                    points[0][1],
                    points[2][0] - points[0][0],
                    points[2][1] - points[0][1]
                ));
                this.canvas.setImage(this.state.image.base64.full);
            }

            ParsingAPI.sendRequest('/api/image',
                JSON.stringify({
                    image: FileHelper.parseDataURL(this.canvas.getImageDataURL()).data
                })
            )
            .then((result) => {
                this.image.id = result.imageId;
            });

            this.canvas.setElements([]);
        }

        if (stageId > 0) this.toolbar.selectGroup(stageId - 1);
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

        if (stageId > 0) this.toolbar.selectGroup(stageId - 1);
    }

    initBtns() {
        let imgEditBtn = document.getElementById('img-edit-btn');
        let self = this;
        imgEditBtn.onclick = function () {
            self.state.transform.rotation = 0;
            self.state.transform.flipX = false;
            self.state.transform.flipY = false;
            let points = self.drawCanvas.convertPointsToImageCanvasSize(self.state.transform.rectangle);
            let cutOptions = ImageHelper.prepareRectanglePoints(points);
            let cutSrc = ImageHelper.cutImage(self.imageRender.canvas, cutOptions.x, cutOptions.y, cutOptions.w, cutOptions.h);
            self.imageRender.setImage(cutSrc);
            self.updateFramingView();
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
