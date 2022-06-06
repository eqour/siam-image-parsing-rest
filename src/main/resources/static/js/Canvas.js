class Canvas {
    constructor(props) {
        this.htmlCanvas = props.canvas;
        this.htmlCanvas.width = props.initWidth;
        this.htmlCanvas.height = props.initHeight;
        this.ctx = this.htmlCanvas.getContext('2d');
        this.magnifier = props.magnifier;
        this.elements = [];
        this.imageHistory = [];
        this.setImageState(Canvas.defaultImageState());

        const self = this;
        $(this.htmlCanvas).mousemove((e) => self.magnifier.magnify(self.htmlCanvas, e.offsetX, e.offsetY));

        // Handle dragging canvas objects
        this.mouseState = {
            isDown: false,
            target: null
        }

        $(this.htmlCanvas).mousedown((e) => {
            self.mouseState.isDown = true;
            for (let i = 0; i < self.elements.length; i++) {
                const target = self.elements[i].getTarget(e.offsetX, e.offsetY);
                if (target !== null) {
                    self.mouseState.target = target;
                    break;
                }
            }
        });

        $(this.htmlCanvas).mouseup((e) => {
            self.mouseState.isDown = false;
            self.mouseState.target = null;
        });

        $(this.htmlCanvas).mouseleave((e) => {
            self.mouseState.isDown = false;
            self.mouseState.target = null;
        });

        $(this.htmlCanvas).mousemove((e) => {
            if (self.mouseState.isDown) {
                const element = self.mouseState.target;
                if (element !== undefined && element !== null) {
                    element.setPosition(e.offsetX, e.offsetY);
                    self.render();
                }
            }
        });
    }

    async setImage(src) {
        const image = new Image();
        const self = this;
        return new Promise((reslove, reject) => {
            image.onload = function() {
                const cnvWidth = self.htmlCanvas.width;
                const cnvHeight = self.htmlCanvas.height;
                const size = self.calcImageSize(cnvWidth, cnvHeight, image.width, image.height);
                self.cachedImage = {
                    image: image,
                    w: size.width,
                    h: size.height
                };
                self.setImageState(Canvas.defaultImageState());
                self.render();
                reslove();
            }
            image.src = src;
        });
    }

    render() {
        this.clear();
        this.renderImage();
        this.renderElements();
    }

    renderImage() {
        const state = this.imageState;

        this.ctx.save();

        if (state !== undefined) {
            if (state.rotation !== undefined) this.rotate(state.rotation);
            if (state.flipX !== undefined || state.flipY !== undefined) this.flip(state.flipX, state.flipY);
        }
        
        this.drawCachedImage();
        this.ctx.restore();
    }

    renderElements() {
        for (let i = 0; i < this.elements.length; i++) this.elements[i].render();
    }

    drawCachedImage() {
        if (this.cachedImage === undefined) return;
        const image = this.cachedImage.image;
        const w = this.cachedImage.w;
        const h = this.cachedImage.h;
        const cnvWidth = this.htmlCanvas.width;
        const cnvHeight = this.htmlCanvas.height;
        this.ctx.drawImage(
            image,
            (cnvWidth - w) / 2,
            (cnvHeight - h) / 2,
            w,
            h
        );
    }

    flip(flipX, flipY) {
        if (flipX === undefined) flipX = 0;
        if (flipY === undefined) flipY = 0;
        const cnvWidth = this.htmlCanvas.width;
        const cnvHeight = this.htmlCanvas.height;
        this.ctx.translate(flipX ? cnvWidth : 0, flipY ? cnvHeight : 0);
        this.ctx.scale(flipX ? -1 : 1, flipY ? -1 : 1);
    }

    rotate(deg) {
        this.ctx.translate(this.htmlCanvas.width / 2, this.htmlCanvas.height / 2);
        this.ctx.rotate(deg * Math.PI / 180);
        this.ctx.translate(-this.htmlCanvas.width / 2, -this.htmlCanvas.height / 2);
    }

    clear() {
        this.ctx.clearRect(0, 0, this.htmlCanvas.width, this.htmlCanvas.height);
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

    setElements(elements) {
        this.elements = elements;
        this.render();
    }

    getImageDataURL() {
        this.clear();
        this.renderImage();
        const data = this.htmlCanvas.toDataURL();
        this.renderElements();
        return data;
    }

    getSubImageDataURL(x, y, w, h) {
        this.clear();
        this.renderImage();
        const tempCanvas = document.createElement('canvas');
        tempCanvas.width = w;
        tempCanvas.height = h;
        tempCanvas.getContext('2d').drawImage(this.htmlCanvas, x, y, w, h, 0, 0, w, h);
        this.renderElements();
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

    saveImage() {
        this.imageHistory.push({
            cachedImage: this.cachedImage,
            imageState: this.imageState
        });
    }

    restoreImage() {
        const prevData = this.imageHistory.pop();
        this.cachedImage = prevData.cachedImage;
        this.setImageState(prevData.imageState);
    }
    
    static defaultImageState() {
        return {
            rotation: 0,
            flipX: false,
            flipY: false
        };
    }
}

export default Canvas;
