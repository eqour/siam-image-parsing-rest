import ImageHelper from "../helper/ImageHelper.js";

class DrawCanvas {
    constructor(canvas, imageCanvas, state, magnifier) {
        this.imageCanvas = imageCanvas;
        this.canvas = canvas;
        this.clickRadius = 20;
        this.ctx = this.canvas.getContext('2d');
        this.state = state;
        this.magnifier = magnifier;
        this.bindWithMagnifier();
        this.drawAction = null;
        this.magnifyOpacity = 1.0;
    }

    bindWithMagnifier() {
        let self = this;
        this.canvas.addEventListener('mousemove', function (e) {
            let p = self.convertPointToImageCanvas([e.offsetX, e.offsetY]);
            self.magnifier.magnify(self.imageCanvas, self.canvas, p[0], p[1], e.offsetX, e.offsetY, self.magnifyOpacity);
        })
    }

    initDrawRectangleSelect() {
        this.magnifyOpacity = 1.0;
        let points = this.state.transform.rectangle;
        this.drawAction = function () {
            this.drawPolygone(points);
        };
        let self = this;
        let selected = null;
        let isDown = false;
        this.canvas.onmousedown = function (e) {
            isDown = true;
            selected = self.findElement(points, e.offsetX, e.offsetY);
        }
        this.canvas.onmouseup = function (e) {
            isDown = false;
            selected = null;
        }
        this.canvas.onmouseleave = function () {
            isDown = false;
            selected = null;
        }
        this.canvas.onmousemove = function (e) {
            if (isDown && selected != null) {
                let relative = self.convertPixelToRelativePoint([e.offsetX, e.offsetY]);
                selected[1][0] = relative[0];
                selected[1][1] = relative[1];
                let index = selected[0];
                if (index === 0) {
                    points[3][1] = relative[1];
                    points[1][0] = relative[0];
                } else if (index === 1) {
                    points[2][1] = relative[1];
                    points[0][0] = relative[0];
                } else if (index === 2) {
                    points[1][1] = relative[1];
                    points[3][0] = relative[0];
                } else if (index === 3) {
                    points[0][1] = relative[1];
                    points[2][0] = relative[0];
                }
                self.drawAction();
            }
        }
    }

    initDrawPolygonSelect() {
        this.magnifyOpacity = 1.0;
        let points = this.state.transform.perspective;
        this.drawAction = function () {
            this.drawPolygone(points);
        };
        let self = this;
        let selected = null;
        let isDown = false;
        this.canvas.onmousedown = function (e) {
            isDown = true;
            selected = self.findElement(points, e.offsetX, e.offsetY);
        }
        this.canvas.onmouseup = function (e) {
            isDown = false;
            selected = null;
        }
        this.canvas.onmouseleave = function () {
            isDown = false;
            selected = null;
        }
        this.canvas.onmousemove = function (e) {
            if (isDown && selected != null) {
                let pixel = self.convertPixelToRelativePoint([e.offsetX, e.offsetY]);
                selected[1][0] = pixel[0];
                selected[1][1] = pixel[1];
                self.drawAction();
            }
        }
    }

    initDisableDraw() {
        this.drawAction = function () {};
        this.canvas.onmousedown = function (e) {}
        this.canvas.onmouseup = function (e) {}
        this.canvas.onmouseleave = function () {}
        this.canvas.onmousemove = function (e) {}
        this.clear();
    }

    initDrawRectangles(isAdd) {
        this.magnifyOpacity = 0.5;
        this.clear();
        if (this.state.selection.selectImage == null) {
            this.state.setSelectImage(ImageHelper.cloneCanvas(this.canvas));
        }
        this.drawAction = function () {
            self.clear();
            this.ctx.globalCompositeOperation = 'source-over';
            self.ctx.drawImage(self.state.selection.selectImage, 0, 0);
        };
        let self = this;
        let isDown = false;

        let start = null;
        this.canvas.onmousedown = function (e) {
            start = [e.offsetX, e.offsetY];
            isDown = true;
        }
        this.canvas.onmouseup = function (e) {
            isDown = false;
            self.state.setSelectImage(ImageHelper.cloneCanvas(self.canvas));
        }
        this.canvas.onmouseleave = function () {
            isDown = false;
        }
        this.canvas.onmousemove = function (e) {
            if (isDown) {
                self.drawAction();
                let end = [e.offsetX, e.offsetY];
                if (!isAdd) {
                    self.ctx.globalCompositeOperation = 'destination-out';
                }
                if (start != null) {
                    self.drawRectangle(start, end, '#F02121');
                }
            }
        }
        this.drawAction();
    }

    initDrawPencil(isAdd) {
        this.magnifyOpacity = 0.5;
        this.clear();
        let self = this;
        let isDown = false;
        if (this.state.selection.selectImage == null) {
            this.state.setSelectImage(ImageHelper.cloneCanvas(this.canvas));
        }
        let current = null;
        this.drawAction = function () {
            self.clear();
            this.ctx.globalCompositeOperation = 'source-over';
            self.ctx.drawImage(self.state.selection.selectImage, 0, 0);
        };

        let pos = null;
        this.canvas.onmousedown = function (e) {
            isDown = true;
            pos = [e.offsetX, e.offsetY];
        }
        this.canvas.onmouseup = function (e) {
            isDown = false;
            self.state.setSelectImage(ImageHelper.cloneCanvas(self.canvas));
        }
        this.canvas.onmouseleave = function () {
            isDown = false;
        }
        this.canvas.onmousemove = function (e) {
            if (isDown) {
                let current = [e.offsetX, e.offsetY];
                let radius;
                if (!isAdd) {
                    radius = self.state.selection.eraser;
                    self.ctx.globalCompositeOperation = 'destination-out';
                } else {
                    radius = self.state.selection.pencil;
                }
                if (pos != null) {
                    self.drawPencil(pos, current, radius, '#F02121');
                }
            }
        }
        this.drawAction();
    }

    initDrawAxes() {
        this.magnifyOpacity = 1.0;
        function checkPoint(point) {
            return isNaN(point[0]) || isNaN(point[1]);
        }
        if (checkPoint(this.state.axes.x.start) || checkPoint(this.state.axes.x.end) || checkPoint(this.state.axes.y.start) || checkPoint(this.state.axes.y.end)) {
            this.state.setAxesDefaultStartEndByCanvas(this.imageCanvas);
        }
        this.drawAction = function () {
            this.clear();
            this.ctx.globalCompositeOperation = 'source-over';
            let axisY = this.convertImagePixelToDrawPixelAll([this.state.axes.y.start, this.state.axes.y.end]);
            let axisX = this.convertImagePixelToDrawPixelAll([this.state.axes.x.start, this.state.axes.x.end]);
            this.drawLine(axisY, '#1ABF21');
            this.drawLine(axisX, '#0F9AFF');
        };
        let self = this;
        let selected = null;
        let isDown = false;
        this.canvas.onmousedown = function (e) {
            isDown = true;
            let points = [self.state.axes.x.start, self.state.axes.x.end, self.state.axes.y.start, self.state.axes.y.end];
            selected = self.findElementInImage(points, e.offsetX, e.offsetY);
        }
        this.canvas.onmouseup = function (e) {
            isDown = false;
            selected = null;
        }
        this.canvas.onmouseleave = function () {
            isDown = false;
            selected = null;
        }
        this.canvas.onmousemove = function (e) {
            if (isDown && selected != null) {
                let pixel = self.convertDrawPixelToImagePixel([e.offsetX, e.offsetY]);
                selected[1][0] = pixel[0];
                selected[1][1] = pixel[1];
                if (selected[0] === 0) {
                    self.state.axes.x.end[1] = pixel[1];
                } else if (selected[0] === 1) {
                    self.state.axes.x.start[1] = pixel[1];
                } else if (selected[0] === 2) {
                    self.state.axes.y.end[0] = pixel[0];
                } else if (selected[0] === 3) {
                    self.state.axes.y.start[0] = pixel[0];
                }
                self.drawAction();
            }
        }
        this.clear();
        this.drawAction();
    }

    initDrawPoints() {
        this.magnifyOpacity = 1.0;
        this.drawAction = function () {
            let points = this.state.parsing.points;
            this.clear();
            let converted = [];
            for (let i = 0; i < points.length; i++) {
                converted.push(this.convertImagePixelToDrawPixel(points[i]));
            }
            this.drawCircles(converted, '#FFFFFF', '#000000', 5);
        };
        let self = this;
        this.canvas.onmousedown = function (e) {
            self.state.parsing.points.push(self.convertDrawPixelToImagePixel([e.offsetX, e.offsetY]));
            self.drawAction();
        }
        this.canvas.onmouseup = function (e) {}
        this.canvas.onmouseleave = function () {}
        this.canvas.onmousemove = function (e) {}
        this.clear();
        this.drawAction();
    }

    initDeletePoints() {
        this.magnifyOpacity = 1.0;
        let self = this;
        this.drawAction = function () {
            let points = this.state.parsing.points;
            this.clear();
            let converted = [];
            for (let i = 0; i < points.length; i++) {
                converted.push(this.convertImagePixelToDrawPixel(points[i]));
            }
            this.drawCircles(converted, '#FFFFFF', '#000000', 5);
        };
        let isDown = false;
        this.canvas.onmousedown = function (e) {
            isDown = true;
            self.state.setParsingPoints(self.removePointsByCircle(self.state.parsing.points, self.state.parsing.eraser, e.offsetX, e.offsetY));
            self.drawAction();
        }
        this.canvas.onmouseup = function (e) {
            isDown = false;
        }
        this.canvas.onmouseleave = function () {
            isDown = false;
            self.drawAction();
        }
        this.canvas.onmousemove = function (e) {
            // self.clear();
            // self.drawCircles([[e.offsetX, e.offsetY]],'transparent','#000000', self.state.parsing.eraser);
            if (isDown) {
                self.state.setParsingPoints(self.removePointsByCircle(self.state.parsing.points, self.state.parsing.eraser, e.offsetX, e.offsetY));
                self.drawAction();
            }
        }
        this.clear();
        this.drawAction();
    }

    removePointsByCircle(points, radius, x ,y) {
        let allWithoutInCircle = [];
        for (let i = 0; i < points.length; i++) {
            let converted = this.convertImagePixelToDrawPixel(points[i]);
            let d = this.distance(x, y, converted[0], converted[1]);
            if (d > radius) {
                allWithoutInCircle.push(points[i]);
            }
        }
        return allWithoutInCircle;
    }

    findElement(points, x, y) {
        let inRadius = [];
        for (let i = 0; i < points.length; i++) {
            let point = points[i];
            let converted = this.convertRelativePointToPixels(point);
            let d = this.distance(converted[0], converted[1], x, y);
            if (d <= this.clickRadius) {
                inRadius.push([d, [i, point]]);
            }
        }
        if (inRadius.length !== 0) {
            inRadius.sort((a,b) => a[0] - b[0]);
            return inRadius[0][1];
        } else {
            return null;
        }
    }

    findElementInImage(points, x, y) {
        let inRadius = [];
        for (let i = 0; i < points.length; i++) {
            let point = points[i];
            let converted = this.convertImagePixelToDrawPixel(point);
            let d = this.distance(converted[0], converted[1], x, y);
            if (d <= this.clickRadius) {
                inRadius.push([d, [i, point]]);
            }
        }
        if (inRadius.length !== 0) {
            inRadius.sort((a,b) => a[0] - b[0]);
            return inRadius[0][1];
        } else {
            return null;
        }
    }

    distance(x1, y1, x2, y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
    }

    drawPolygone(points) {
        this.clear();
        let ctx = this.ctx;
        ctx.beginPath();
        ctx.fillStyle = '#9191917c';
        ctx.strokeStyle = '#000000';
        ctx.lineWidth = 1;
        let corners = points;
        for (let i = 0; i < corners.length; i++) {
            const corner = this.convertRelativePointToPixels(corners[i]);
            if (i === 0) ctx.moveTo(corner[0], corner[1]);
            else ctx.lineTo(corner[0], corner[1]);
        }
        ctx.closePath();
        ctx.fill();
        ctx.stroke();
    }

    drawLine(points, color) {
        let ctx = this.ctx;
        ctx.beginPath();
        ctx.strokeStyle = color;
        ctx.lineWidth = 2;
        for (let i = 0; i < points.length; i++) {
            let corner = points[i];
            if (i === 0) ctx.moveTo(corner[0], corner[1]);
            else ctx.lineTo(corner[0], corner[1]);
        }
        ctx.closePath();
        ctx.stroke();
        this.drawCircles(points, color, '#FFFFFF', 4);
    }

    drawCircles(points, fillColor, strokeColor, radius) {
        let ctx = this.ctx;
        ctx.strokeStyle = strokeColor;
        ctx.fillStyle = fillColor;
        ctx.lineWidth = 1;
        for (let i = 0; i < points.length; i++) {
            ctx.beginPath();
            let corner = points[i];
            ctx.arc(corner[0], corner[1], radius, 0, 2 * Math.PI, false);
            ctx.fill();
            ctx.stroke();
        }
        ctx.closePath();
    }

    convertRelativePointToPixels(point) {
        let w = this.canvas.width;
        let h = this.canvas.height;
        return [ w * point[0], h * point[1]];
    }

    convertPixelToRelativePoint(pixel) {
        let w = this.canvas.width;
        let h = this.canvas.height;
        return [pixel[0] / w, pixel[1] / h];
    }

    convertImagePixelToDrawPixel(imagePixel) {
        let w = this.imageCanvas.width;
        let h = this.imageCanvas.height;
        return [Math.round(imagePixel[0] / w * this.canvas.width), Math.round(imagePixel[1] / h * this.canvas.height)];
    }

    convertImagePixelToDrawPixelAll(pixels) {
        let converted = [];
        for (let i = 0; i < pixels.length; i++) {
            converted.push(this.convertImagePixelToDrawPixel(pixels[i]));
        }
        return converted;
    }

    convertDrawPixelToImagePixel(drawPixel) {
        let w = this.canvas.width;
        let h = this.canvas.height;
        return [Math.round(drawPixel[0] / w * this.imageCanvas.width), Math.round(drawPixel[1] / h * this.imageCanvas.height)];
    }

    resize(width, height) {
        this.clear();
        this.canvas.width = width;
        this.canvas.height = height;
        this.drawAction();
    }

    clear() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }

    convertPointsToImageCanvasSize(points) {
        let converted = [];
        for (let i = 0; i < points.length; i++) {
            converted.push(this.convertPointToImageCanvas(this.convertRelativePointToPixels(points[i])));
        }
        return converted;
    }

    convertPointToImageCanvas(pointOnCanvas) {
        let x = pointOnCanvas[0] / this.canvas.width * this.imageCanvas.width;
        let y = pointOnCanvas[1] / this.canvas.height * this.imageCanvas.height;
        return [Math.round(x), Math.round(y)];
    }

    drawRectangle(start, end, color) {
        this.ctx.fillStyle = color;
        this.ctx.fillRect(start[0], start[1], end[0] - start[0], end[1] - start[1]);
    }

    drawPencil(pos, current, radius, color) {
        this.ctx.beginPath();
        this.ctx.lineWidth = radius;
        this.ctx.lineCap = 'round';
        this.ctx.strokeStyle = color;
        this.ctx.moveTo(pos[0], pos[1]);
        pos[0] = current[0];
        pos[1] = current[1];
        this.ctx.lineTo(pos[0], pos[1]);
        this.ctx.stroke();
    }
}

export default DrawCanvas