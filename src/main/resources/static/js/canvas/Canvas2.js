class ImageRender {
    constructor(canvas, sizeElement, bindingCanvas, state) {
        this.state = state;
        this.sizeElement = sizeElement;
        this.bindingCanvas = bindingCanvas;
        this.canvas = canvas;
        this.ctx = this.canvas.getContext('2d');
    }

    async setImage(src) {
        const image = new Image();
        const self = this;
        return new Promise((reslove, reject) => {
            image.onload = function() {
                self.canvas.width = image.width;
                self.canvas.height = image.height;
                self.state.corrections.image = {
                    image: image,
                    w: image.width,
                    h: image.height
                };
                self.setImageState(ImageRender.defaultImageState());
                self.render();
                reslove();
            }
            image.src = src;
        });
    }

    render() {
        this.clear();
        this.renderImage();
        this.resizeCanvasStyle();
    }

    resizeCanvasStyle() {
        let rect = this.sizeElement.getBoundingClientRect();
        let size = this.calcImageSize(rect.width, rect.height, this.canvas.width,this.canvas.height);
        this.canvas.style.width = size.width + "px";
        this.canvas.style.height = size.height + "px";
        this.bindingCanvas.resize(size.width, size.height);
    }

    renderImage() {
        const state = this.state;
        this.ctx.save();
        this.rotate(state.transform.rotation);
        this.flip(state.transform.flipX, state.transform.flipY);
        this.draw();
    }

    flip(flipX, flipY) {
        if (flipX === undefined) flipX = 0;
        if (flipY === undefined) flipY = 0;
        this.ctx.scale(flipX ? -1 : 1, flipY ? -1 : 1);
    }

    rotate(degrees) {
        let image = this.state.corrections.image.image;
        let degr = Math.abs(degrees % 90);
        let radr = this.toRad(degr);
        let rad = this.toRad(degrees);
        let w = Math.ceil(image.width * Math.cos(radr) + image.height * Math.sin(radr));
        let h = Math.ceil(image.width * Math.sin(radr) + image.height * Math.cos(radr));
        this.canvas.width = w;
        this.canvas.height = h;
        this.ctx.translate(w / 2, h / 2);
        this.ctx.rotate(rad);
    }

    draw() {
        let image = this.state.corrections.image.image;
        this.ctx.drawImage(image, -image.width / 2, -image.height / 2);
        this.ctx.restore();
    }

    calcImageSize(cnvW, cnvH, imgW, imgH) {
        let nImgW = imgW;
        let nImgH = imgH;
        const ratio = imgW / imgH;
        nImgW = cnvW;
        nImgH = nImgW / ratio;
        if (nImgH > cnvH) {
            nImgH = cnvH;
            nImgW = nImgH * ratio;
        }
        return {
            width: nImgW,
            height: nImgH
        };
    }

    toRad(degree) {
        return degree * Math.PI / 180.0;
    }

    clear() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }

    getImageDataURL() {
        this.clear();
        this.renderImage();
        return this.canvas.toDataURL();
    }

    getSubImageDataURL(x, y, w, h) {
        this.clear();
        this.renderImage();
        const tempCanvas = document.createElement('canvas');
        tempCanvas.width = w;
        tempCanvas.height = h;
        tempCanvas.getContext('2d').drawImage(this.canvas, x, y, w, h, 0, 0, w, h);
        return tempCanvas.toDataURL();
    }

    setImageState(imageState) {
        this.imageState = imageState;

        $('#rotation-range').val(imageState.rotation);
        $('#rotation-text').val(imageState.rotation);

        if (imageState.flipX) $('#flip-horizontal').addClass('item-active');
        else $('#flip-horizontal').removeClass('item-active');

        if (imageState.flipY) $('#flip-vertical').addClass('item-active');
        else $('#flip-vertical').removeClass('item-active');
    }

    static defaultImageState() {
        return {
            rotation: 0,
            flipX: false,
            flipY: false
        };
    }
}

export default ImageRender;
