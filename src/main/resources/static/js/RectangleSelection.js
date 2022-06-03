import QuadSelection from './QuadSelection.js';
import Square from './Square.js';

class RectangleSelection extends QuadSelection {
    constructor(props) {
        super(props);

        this.corners = [
            new Square({ ctx: this.ctx, x: this.x, y: this.y, size: 12 }),
            new Square({ ctx: this.ctx, x: this.x + 100, y: this.y, size: 12 }),
            new Square({ ctx: this.ctx, x: this.x + 100, y: this.y + 100, size: 12 }),
            new Square({ ctx: this.ctx, x: this.x, y: this.y + 100, size: 12 })
        ];

        for (let i = 0; i < this.corners.length; i++) {
            const c = this.corners;
            this.corners[i].onPositionChanged.add(function(event) {
                const index = c.indexOf(event.target);
                switch (index) {
                    case 0:
                        c[1].setPosition(c[1].x, event.target.y, false);
                        c[3].setPosition(event.target.x, c[3].y, false);
                        break;
                    case 1:
                        c[0].setPosition(c[0].x, event.target.y, false);
                        c[2].setPosition(event.target.x, c[2].y, false);
                        break;
                    case 2:
                        c[3].setPosition(c[3].x, event.target.y, false);
                        c[1].setPosition(event.target.x, c[1].y, false);
                        break;
                    case 3:
                        c[2].setPosition(c[2].x, event.target.y, false);
                        c[0].setPosition(event.target.x, c[0].y, false);
                        break;
                }
            });
        }
    }
}

export default RectangleSelection;
