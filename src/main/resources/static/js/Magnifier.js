class Magnifier {
    constructor(props) {
        this.canvas = props.magnifier;
        this.ctx = this.canvas.getContext('2d');
    }

    magnify(sourceCanvas, x, y) {
        this.ctx.clearRect(0, 0, 300, 300);
        this.ctx.drawImage(sourceCanvas, x - 75, y - 75, 150, 150, 0, 0, 300, 300);
    }
}

export default Magnifier;
