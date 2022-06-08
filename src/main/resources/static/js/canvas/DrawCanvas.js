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
    }

    bindWithMagnifier() {
        let self = this;
        this.canvas.addEventListener('mousemove', function (e) {
            let p = self.convertPointToImageCanvas({x: e.offsetX, y: e.offsetY});
            self.magnifier.magnify(self.imageCanvas, p[0], p[1]);
        })
    }

    initDrawRectangleSelect() {
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
                let pixel = self.convertPixelToPoint([e.offsetX, e.offsetY]);
                selected[1][0] = pixel.x;
                selected[1][1] = pixel.y;
                let index = selected[0];
                if (index === 0) {
                    points[3][1] = pixel.y;
                    points[1][0] = pixel.x;
                } else if (index === 1) {
                    points[2][1] = pixel.y;
                    points[0][0] = pixel.x;
                } else if (index === 2) {
                    points[1][1] = pixel.y;
                    points[3][0] = pixel.x;
                } else if (index === 3) {
                    points[0][1] = pixel.y;
                    points[2][0] = pixel.x;
                }
                self.drawAction();
            }
        }
    }

    initDrawPolygonSelect() {
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
                let pixel = self.convertPixelToPoint([e.offsetX, e.offsetY]);
                selected[1][0] = pixel.x;
                selected[1][1] = pixel.y;
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

    initDrawAxes() {
        let xAxesPoints = [this.state.axes.x.start, this.state.axes.x.end];
        let yAxesPoints = [this.state.axes.y.start, this.state.axes.y.end];
        let points = [this.state.axes.x.start, this.state.axes.x.end, this.state.axes.y.start, this.state.axes.y.end];
        this.drawAction = function () {
            this.clear();
            this.drawLine(yAxesPoints, '#1ABF21');
            this.drawLine(xAxesPoints, '#0F9AFF');
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
                let pixel = self.convertPixelToPoint([e.offsetX, e.offsetY]);
                selected[1][0] = pixel.x;
                selected[1][1] = pixel.y;
                self.drawAction();
            }
        }
        this.clear();
        this.drawAction();
    }

    initDrawPoints() {
        let points = this.state.parsing.points;
        this.drawAction = function () {
            this.clear();
            let converted = [];
            for (let i = 0; i < points.length; i++) {
                converted.push(this.convertImagePixelToDrawPixel(points[i]));
            }
            this.drawCircles(converted, '#FFFFFF', '#000000', 5);
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
                let pixel = self.convertPixelToPoint([e.offsetX, e.offsetY]);
                selected[1][0] = pixel.x;
                selected[1][1] = pixel.y;
                self.drawAction();
            }
        }
        this.clear();
        this.drawAction();
    }

    findElement(points, x, y) {
        let inRadius = [];
        for (let i = 0; i < points.length; i++) {
            let point = points[i];
            let converted = this.convertPointToPixels(point);
            let d = this.distance(converted.x, converted.y, x, y);
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
            const corner = this.convertPointToPixels(corners[i]);
            if (i === 0) ctx.moveTo(corner.x, corner.y);
            else ctx.lineTo(corner.x, corner.y);
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
        let corners = points;
        let converted = [];
        for (let i = 0; i < corners.length; i++) {
            let corner = this.convertPointToPixels(corners[i]);
            converted.push(corner);
            if (i === 0) ctx.moveTo(corner.x, corner.y);
            else ctx.lineTo(corner.x, corner.y);
        }
        ctx.closePath();
        ctx.stroke();
        this.drawCircles(converted, color, '#FFFFFF', 4);
    }

    drawCircles(points, fillColor, strokeColor, radius) {
        let ctx = this.ctx;
        ctx.strokeStyle = strokeColor;
        ctx.fillStyle = fillColor;
        ctx.lineWidth = 1;
        for (let i = 0; i < points.length; i++) {
            ctx.beginPath();
            let corner = points[i];
            ctx.arc(corner.x, corner.y, radius, 0, 2 * Math.PI, false);
            ctx.fill();
            ctx.stroke();
        }
        ctx.closePath();
    }

    convertPointToPixels(point) {
        let w = this.canvas.width;
        let h = this.canvas.height;
        return {x: w * point[0], y: h * point[1]};
    }
    convertPixelToPoint(pixel) {
        let w = this.canvas.width;
        let h = this.canvas.height;
        return {x: pixel[0] / w, y: pixel[1] / h}
    }

    convertImagePixelToDrawPixel(pixel) {
        let w = this.canvas.width;
        let h = this.canvas.height;
        return {x: pixel[0] / w * this.canvas.width, y: pixel[1] / h * this.canvas.height}
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
            converted.push(this.convertPointToImageCanvas(this.convertPointToPixels(points[i])));
        }
        return converted;
    }

    convertPointToImageCanvas(pointOnCanvas) {
        let x = pointOnCanvas.x / this.canvas.width * this.imageCanvas.width;
        let y = pointOnCanvas.y / this.canvas.height * this.imageCanvas.height;
        return [Math.round(x), Math.round(y)];
    }
}

export default DrawCanvas