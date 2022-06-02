class Canvas {
    constructor(props) {
        this.htmlCanvas = props.canvas;
        this.htmlCanvas.width = props.initWidth;
        this.htmlCanvas.height = props.initHeight;
        this.ctx = this.htmlCanvas.getContext('2d');
        this.magnifier = props.magnifier;

        const self = this;
        $(this.htmlCanvas).on('mousemove', (e) => self.magnifier.magnify(self.htmlCanvas, e.offsetX, e.offsetY));
    }

    renderImage(src) {
        const image = new Image();
        const self = this;
        image.onload = function() {
            const cnvWidth = self.htmlCanvas.width;
            const cnvHeight = self.htmlCanvas.height;
            const size = self.calcImageSize(cnvWidth, cnvHeight, image.width, image.height);
            self.ctx.drawImage(
                image, (cnvWidth - size.width) / 2,
                (cnvHeight - size.height) / 2,
                size.width,
                size.height
            );
            self.cachedImage = {
                image: image,
                w: size.width,
                h: size.height
            };
        }
        image.src = src;
    }

    renderCachedImage() {
        if (this.cachedImage === undefined) return;
        const image = this.cachedImage.image;
        const w = this.cachedImage.w;
        const h = this.cachedImage.h;
        const cnvWidth = this.htmlCanvas.width;
        const cnvHeight = this.htmlCanvas.height;
        this.ctx.drawImage(
            image, (cnvWidth - w) / 2,
            (cnvHeight - h) / 2,
            w,
            h
        );
    }

    rotate(deg) {
        this.ctx.save();
        this.clear();
        this.ctx.translate(this.htmlCanvas.width / 2, this.htmlCanvas.height / 2);
        this.ctx.rotate(deg * Math.PI / 180);
        this.ctx.translate(-this.htmlCanvas.width / 2, -this.htmlCanvas.height / 2);
        this.renderCachedImage();
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

    clear() {
        this.ctx.clearRect(0, 0, this.htmlCanvas.width, this.htmlCanvas.height);
    }
}

export default Canvas;
