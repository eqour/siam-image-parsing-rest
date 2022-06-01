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

    async renderImage(src) {
        return new Promise((resolve, reject) => {
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
                resolve();
            }
            image.src = src;
        });
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

    magnify(event) {
        const x = event.offsetX;
        const y = event.offsetY;
        this.magnifierCtx.clearRect(0, 0, 300, 300);
        this.magnifierCtx.drawImage(this.htmlCanvas, x - 70, y - 70, 140, 140, 0, 0, 300, 300);
    }
}

export default Canvas;
