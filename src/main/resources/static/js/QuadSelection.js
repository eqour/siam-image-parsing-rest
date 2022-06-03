import CanvasElement from './CanvasElement.js';
import Circle from './Circle.js';

class QuadSelection extends CanvasElement {
    constructor(props) {
        super(props);

        this.corners = [
            new Circle({ ctx: this.ctx, x: this.x, y: this.y, radius: 6 }),
            new Circle({ ctx: this.ctx, x: this.x + 100, y: this.y, radius: 6 }),
            new Circle({ ctx: this.ctx, x: this.x + 100, y: this.y + 100, radius: 6 }),
            new Circle({ ctx: this.ctx, x: this.x, y: this.y + 100, radius: 6 })
        ];
    }

    render() {
        const ctx = this.ctx;

        ctx.beginPath();
        ctx.fillStyle = '#9191917c';
        ctx.strokeStyle = '#000000';
        ctx.lineWidth = 1;

        ctx.beginPath();
        for (let i = 0; i < this.corners.length; i++) {
            const corner = this.corners[i];
            if (i === 0) ctx.moveTo(corner.x, corner.y);
            else ctx.lineTo(corner.x, corner.y);
        }
        ctx.closePath();
        ctx.fill();
        ctx.stroke();

        for (let i = 0; i < this.corners.length; i++) {
            this.corners[i].render();
        }
    }

    isIncluded(x, y) {
        for (let i = 0; i < this.corners.length; i++) {
            if (this.corners[i].isIncluded(x, y)) {
                return true;
            }
        }
        return false;
    }

    getTarget(x, y) {
        for (let i = 0; i < this.corners.length; i++) {
            if (this.corners[i].isIncluded(x, y)) {
                return this.corners[i].getTarget(x, y);
            }
        }
        return null;
    }
}

export default QuadSelection;
