import Canvas from './Canvas.js';
import FileDropper from './FileDropper.js';
import FileHelper from './FileHelper.js';
import Magnifier from './Magnifier.js';
import StagesPanel from './StagesPanel.js';

class App {
    constructor(props) {
        this.image = undefined;
        this.stagesPanel = new StagesPanel({
            stages: $('.stage')
        });

        const dropper = new FileDropper({
            dropArea: $('#drop-area').get(0),
            fileInput: $('#file-input').get(0)
        });
        dropper.onFileLoaded.add((e) => this.imageLoadedHandler(e));

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
        $('#drop-area').addClass('hidden');
        $('#canvas-wrapper').removeClass('hidden');
        $('#sidebar').removeClass('hidden');

        this.magnifier = new Magnifier({
            magnifier: $('#magnifier').get(0)
        });

        this.canvas = new Canvas({
            canvas: $('#canvas').get(0),
            initWidth: parseInt($('#canvas-wrapper').css('width')),
            initHeight: parseInt($('#canvas-wrapper').css('height')),
            magnifier: this.magnifier
        });

        this.canvas.renderImage(this.image.base64.full).then(() => {
            this.stagesPanel.nextStage();
        });
    }
}

$(document).ready(new App());

export default App;
