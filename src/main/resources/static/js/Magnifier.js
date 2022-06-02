class Magnifier {
    constructor(props) {
        this.canvas = props.magnifier;
        this.ctx = this.canvas.getContext('2d');
        this.coordXLabel = props.coordXLabel;
        this.coordYLabel = props.coordYLabel;
    }

    magnify(sourceCanvas, x, y) {
        this.ctx.clearRect(0, 0, 300, 300);
        this.ctx.drawImage(sourceCanvas, x - 75, y - 75, 150, 150, 0, 0, 300, 300);
        this.coordXLabel.innerText = Math.round(x, 3);
        this.coordYLabel.innerText = Math.round(y, 3);
    }
}

export default Magnifier;