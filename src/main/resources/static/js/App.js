import Canvas from './Canvas.js';
import FileDropper from './FileDropper.js';
import FileHelper from './FileHelper.js';
import Magnifier from './Magnifier.js';
import ParsingAPI from './ParsingAPI.js';
import StagesPanel from './StagesPanel.js';
import Toolbar from './Toolbar.js';

const IMAGE_DATA_PREFIX = 'data:image/png;base64,';

class App {
    constructor(props) {
        this.image = undefined;
        this.applicationContext = {};

        this.stagesPanel = new StagesPanel({
            stages: $('.item')
        });
        this.stagesPanel.onNextStage.add((e) => this.nextStageHandler(e));
        this.stagesPanel.onPrevStage.add((e) => this.prevStageHandler(e));

        $('#btn-prev').click(() => this.stagesPanel.prevStage());
        $('#btn-next').click(() => this.stagesPanel.nextStage());

        const dropper = new FileDropper({
            dropArea: $('#drop-area').get(0),
            fileInput: $('#file-input').get(0)
        });
        dropper.onFileLoaded.add((e) => this.imageLoadedHandler(e));

        this.toolbar = new Toolbar({
            searchPrefix: 'tool-group',
            appContext: this.applicationContext
        });

        // Handle toolbar input
        const self = this;
        $('#rotation-range').on('input', (e) => {
            $('#rotation-text').val(e.currentTarget.value);
            const istate = self.canvas.imageState;
            istate.rotation = parseInt(e.currentTarget.value);
            self.canvas.render();
        });

        $('#rotation-text').on('input', (e) => {
            $('#rotation-range').val(e.currentTarget.value);
            const istate = self.canvas.imageState;
            istate.rotation = parseInt(e.currentTarget.value);
            self.canvas.render();
        });

        $('#flip-horizontal').click((e) => {
            $(e.currentTarget).toggleClass('item-active');
            const istate = self.canvas.imageState;
            istate.flipX = !istate.flipX;
            self.canvas.render();
        });

        $('#flip-vertical').click((e) => {
            $(e.currentTarget).toggleClass('item-active');
            const istate = self.canvas.imageState;
            istate.flipY = !istate.flipY;
            self.canvas.render();
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

        console.log('app initialized');
    }

    imageLoadedHandler(event) {
        const file = event.file;
        this.image = {
            file: file
        }
        FileHelper.fileToBase64Async(file).then((base64) => {
            this.processBase64Result(base64);
        });
    }

    processBase64Result(base64) {
        this.image = {
            file: this.image.file,
            base64: FileHelper.parseDataURL(base64)
        }
        this.afterFileUpload();
    }

    afterFileUpload() {
        this.stagesPanel.nextStage();
    }

    nextStageHandler(event) {
        const stageId = event.id;

        if (stageId === 1) {
            $('#drop-area').addClass('hidden');
            $('#canvas-wrapper').removeClass('hidden');
            $('#sidebar').removeClass('hidden');

            if (this.magnifier === undefined) {
                this.magnifier = new Magnifier({
                    magnifier: $('#magnifier').get(0),
                    coordXLabel: $('#coord-x').get(0),
                    coordYLabel: $('#coord-y').get(0)
                });
            }

            if (this.canvas === undefined) {
                this.canvas = new Canvas({
                    canvas: $('#canvas').get(0),
                    initWidth: parseInt($('#canvas-wrapper').css('width')),
                    initHeight: parseInt($('#canvas-wrapper').css('height')),
                    magnifier: this.magnifier
                });
                this.applicationContext.canvas = this.canvas;
            }

            this.canvas.setImage(this.image.base64.full);
        }

        if (stageId === 2) {
            this.canvas.saveImage();

            if (this.toolbar.subIndex === 0) {
                const points = this.canvas.elements[0].getPoints();
                this.image.base64 = FileHelper.parseDataURL(this.canvas.getSubImageDataURL(
                    points[0][0],
                    points[0][1],
                    points[2][0] - points[0][0],
                    points[2][1] - points[0][1]
                ));
                this.canvas.setImage(this.image.base64.full);
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
}

$(document).ready(new App());

export default App;
