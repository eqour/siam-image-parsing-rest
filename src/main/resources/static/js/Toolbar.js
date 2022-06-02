class Toolbar {
    constructor(props) {
        this.groups = this.initGroups(props.searchPrefix);
        this.index = 0;
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
        for (let i = 0; i < this.groups[this.index].sub.length; i++) {
            let element = $(this.groups[this.index].sub[i].element);
            let btn = $(this.groups[this.index].sub[i].button);
            if (id == i) {
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
            if (id == i) {
                element.removeClass('hidden');
                this.selectSubGroup(0);
            } else {
                element.addClass('hidden');
            }
        }
    }
}

export default Toolbar;
