import QuadSelection from './QuadSelection.js';
import RectangleSelection from './RectangleSelection.js';

class Toolbar {
    constructor(props) {
        this.appContext = props.appContext;
        this.groups = this.initGroups(props.searchPrefix);
        this.index = 0;
        this.subIndex = 0;
        $(this.groups[this.index].group).removeClass('hidden');
    }

    initGroups(groupIdPrefix) {
        let result = [];
        for (let i = 0;; i++) {
            const element = document.getElementById(groupIdPrefix + '-' + i);
            if (element == undefined) break;
            let subGroups = [];
            for (let j = 0;; j++) {
                const element = document.getElementById(groupIdPrefix + '-' + i + '-' + j);
                const btn = $('#' + groupIdPrefix + '-' + i + '-btn-' + j);
                btn.click(() => this.selectSubGroup(j));
                if (element == undefined) break;
                subGroups.push({
                    button: btn,
                    element: element
                });
            }
            result.push({
                group: element,
                sub: subGroups,
            });
        }
        return result;
    }

    selectSubGroup(id) {
        this.subIndex = id;
        this.handleSelection(this.index, id);
        for (let i = 0; i < this.groups[this.index].sub.length; i++) {
            let element = $(this.groups[this.index].sub[i].element);
            let btn = $(this.groups[this.index].sub[i].button);
            if (id === i) {
                element.removeClass('hidden');
                btn.addClass('item-active');
            } else {
                element.addClass('hidden');
                btn.removeClass('item-active');
            }
        }
    }

    selectGroup(id) {
        this.index = id;
        for (let i = 0; i < this.groups.length; i++) {
            let element = $(this.groups[i].group);
            if (id === i) {
                element.removeClass('hidden');
                this.selectSubGroup(0);
            } else {
                element.addClass('hidden');
            }
        }
    }

    handleSelection(groupId, subGroupId) {
        const canvas = this.appContext.canvas;
        const ctx = canvas.ctx;

        if (groupId === 0 && subGroupId === 0) {
            canvas.setElements([
                new RectangleSelection({
                ctx: ctx,
                x: canvas.htmlCanvas.width / 2 - 50,
                y: canvas.htmlCanvas.height / 2 - 50
            })]);
        }

        if (groupId === 0 && subGroupId === 1) {
            canvas.setElements([
                new QuadSelection({
                ctx: ctx,
                x: canvas.htmlCanvas.width / 2 - 50,
                y: canvas.htmlCanvas.height / 2 - 50
            })]);
        }
    }
}

export default Toolbar;
