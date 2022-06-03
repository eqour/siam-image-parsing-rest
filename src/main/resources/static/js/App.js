import Canvas from './Canvas.js';
import FileDropper from './FileDropper.js';
import FileHelper from './FileHelper.js';
import Magnifier from './Magnifier.js';
import StagesPanel from './StagesPanel.js';
import Toolbar from './Toolbar.js';

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
        const splitted = base64.split(',');
        this.image = {
            file: this.image.file,
            base64: {
                prefix: splitted[0] + ',',
                data: splitted[1],
                full: base64
            }
        }
        this.afterFileUpload();
    }

    afterFileUpload() {
        this.stagesPanel.nextStage();
    }

    nextStageHandler(event) {
        if (event.id === 1) {
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
                    magnifier: this.magnifier,
                    imageState: {
                        rotation: 0,
                        flipX: false,
                        flipY: false
                    }
                });
                this.applicationContext.canvas = this.canvas;
            }

            this.canvas.setImage(this.image.base64.full).then(() => this.canvas.render());
        }

        if (event.id > 0) this.toolbar.selectGroup(event.id - 1);
    }

    prevStageHandler(event) {
        if (event.id === 0) {
            $('#drop-area').removeClass('hidden');
            $('#canvas-wrapper').addClass('hidden');
            $('#sidebar').addClass('hidden');
        }
    }
}

$(document).ready(new App());

export default App;
