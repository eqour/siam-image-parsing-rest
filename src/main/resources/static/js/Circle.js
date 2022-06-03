import CanvasElement from './CanvasElement.js';

class Circle extends CanvasElement {
    constructor(props) {
        super(props);
        this.radius = props.radius;
    }

    render() {
        const ctx = this.ctx;

        ctx.beginPath();
        ctx.fillStyle = '#000000';
        ctx.strokeStyle = '#FFFFFF';
        ctx.lineWidth = 2;
        
        ctx.arc(this.x, this.y, this.radius, 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.stroke();
    }

    isIncluded(x, y) {
        const len = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
        return len <= this.radius;
    }
}

export default Circle;
