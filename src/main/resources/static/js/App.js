import FileDropper from './FileDropper.js';
import FileHelper from './FileHelper.js';
import StagesPanel from './StagesPanel.js';

class App {
    constructor(props) {
        this.image = undefined;
        this.stagesPanel = new StagesPanel({
            stages: $('.stage')
        });
    }

    init() {
        const dropper = new FileDropper({
            dropArea: $('#drop-area').get(0),
            fileInput: $('#file-input').get(0)
        });
        console.log('init');
        dropper.onFileLoaded.add((e) => this.imageLoadedHandler(e));
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
        $('#image').attr('src', this.image.base64.full);
        this.stagesPanel.nextStage();
    }
}

$(document).ready(new App().init());

export default App;
