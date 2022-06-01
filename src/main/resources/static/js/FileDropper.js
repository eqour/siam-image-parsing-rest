class FileDropper {
    constructor(props) {
        this.dropArea = props.dropArea;
        this.fileInput = props.fileInput;
        this.onFileLoaded = $.Callbacks();

        $(this.dropArea).on('dragenter', (e) => e.preventDefault());
        $(this.dropArea).on('dragover', (e) => e.preventDefault());
        $(this.dropArea).on('drop', (e) => this.dropFileHandler(e));
        $(this.fileInput).change((e) => this.inputFileHandler(e));
    }

    dropFileHandler(event) {
        event.preventDefault();
        const file = event.originalEvent.dataTransfer.files[0];
        if (file !== undefined) {
            this.onFileLoaded.fire({file: file});
        }
    }

    inputFileHandler(event) {
        const file = event.currentTarget.files[0];
        if (file !== undefined) {
            this.onFileLoaded.fire({file: file});
        }
    }
}

export default FileDropper;
