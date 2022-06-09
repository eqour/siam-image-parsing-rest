class Magnifier {
    constructor(canvas, xLabel, yLabel) {
        this.canvas = canvas;
        this.ctx = this.canvas.getContext('2d');
        this.coordXLabel = xLabel;
        this.coordYLabel = yLabel;
    }

    magnify(imageCanvas, drawCanvas, sourceX, sourceY, drawX, drawY) {
        this.ctx.clearRect(0, 0, 300, 300);
        const dx = imageCanvas.width / parseInt(imageCanvas.style.width);
        const dy = imageCanvas.height / parseInt(imageCanvas.style.height);
        this.ctx.drawImage(imageCanvas, sourceX - 75 * dx, sourceY - 75 * dy, 150 * dx, 150 * dy, 0, 0, 300, 300);
        this.ctx.drawImage(drawCanvas, drawX - 75, drawY - 75, 150, 150, 0, 0, 300, 300);
        this.coordXLabel.innerText = Math.round(sourceX, 3);
        this.coordYLabel.innerText = Math.round(sourceY, 3);
    }
}

export default Magnifier;
