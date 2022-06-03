import CanvasElement from './CanvasElement.js';

class Square extends CanvasElement {
    constructor(props) {
        super(props);
        this.size = props.size;
    }

    render() {
        const ctx = this.ctx;
        const half = Math.round(this.size / 2);

        ctx.beginPath();
        ctx.fillStyle = '#FFFFFF';
        ctx.strokeStyle = '#000000';
        ctx.lineWidth = 2;
        
        ctx.rect(this.x - half, this.y - half, this.size, this.size);
        ctx.fill();
        ctx.stroke();
    }

    isIncluded(x, y) {
        const half = Math.round(this.size / 2);
        return (x >= this.x - half && x <= this.x + this.size - half
            && y >= this.y - half && y <= this.y + this.size - half);
    }
}

export default Square;
