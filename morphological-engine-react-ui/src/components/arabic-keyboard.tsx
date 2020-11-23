import * as React from 'react';
import { Dialog } from 'primereact/dialog';
import { ToggleSelecter } from './toggle-selecter';
import { Button } from 'primereact/button';
import { ArabicLetter } from './model/arabic-letter';
import { RootLetters } from './model/root-letters';
import ArabicButton from './arabic-button';

interface Props {
    rootLetters: RootLetters
    visible: boolean
    onHide(rootLetters?: RootLetters): void
}

interface State {
    rootLetters: RootLetters
    currentIndex: number
    select1: boolean
    select2: boolean
    select3: boolean
    select4: boolean
}

export default class ArabicKeyboard extends React.Component<Props, State> {

    constructor(props: any) {
        super(props);

        this.show = this.show.bind(this);
        this.letterSelected = this.letterSelected.bind(this);
        this.selectLetter = this.selectLetter.bind(this);

        this.state = {
            rootLetters: this.props.rootLetters,
            currentIndex: 0,
            select1: true,
            select2: false,
            select3: false,
            select4: false
        }
    }

    get letters(): ArabicLetter[] {
        return ArabicLetter.arabicLetters;
    }

    private show(): void {
        this.setState({
            rootLetters: this.props.rootLetters,
            currentIndex: 0,
            select1: true,
            select2: false,
            select3: false,
            select4: false
        })
    }

    private resetSelection(): void {
        this.setState({
            rootLetters: this.props.rootLetters,
            currentIndex: 0,
            select1: true,
            select2: false,
            select3: false,
            select4: false
        })
    }

    private selectLetter(letter: ArabicLetter) {
        const rl = this.state.rootLetters;
        switch (this.state.currentIndex) {
            case 0:
                this.setState({
                    rootLetters: new RootLetters(letter, rl.secondRadical, rl.thirdRadical, rl.fourthRadical),
                    currentIndex: 1,
                    select1: false,
                    select2: true
                });
                break;
            case 1:
                this.setState({
                    rootLetters: new RootLetters(rl.firstRadical, letter, rl.thirdRadical, rl.fourthRadical),
                    currentIndex: 2,
                    select2: false,
                    select3: true
                });
                break;
            case 2:
                this.setState({
                    rootLetters: new RootLetters(rl.firstRadical, rl.secondRadical, letter, rl.fourthRadical),
                    currentIndex: 3,
                    select3: false,
                    select4: true
                });
                break;
            case 3:
                this.setState({
                    rootLetters: new RootLetters(rl.firstRadical, rl.secondRadical, rl.thirdRadical, letter),
                    currentIndex: 0,
                    select4: false,
                    select1: true
                });
                break;
        }
    }

    private letterSelected(payload: any) {
        const selected: boolean = payload.selected;
        const index: number = payload.index;
        if (selected) {
            switch (index) {
                case 0:
                    this.setState({
                        currentIndex: 0,
                        select1: true,
                        select2: false,
                        select3: false,
                        select4: false
                    })
                    break;
                case 1:
                    this.setState({
                        currentIndex: 1,
                        select1: false,
                        select2: true,
                        select3: false,
                        select4: false
                    })
                    break;
                case 2:
                    this.setState({
                        currentIndex: 2,
                        select1: false,
                        select2: false,
                        select3: true,
                        select4: false
                    })
                    break;
                case 3:
                    this.setState({
                        currentIndex: 3,
                        select1: false,
                        select2: false,
                        select3: false,
                        select4: true
                    })
                    break;
            }
        } else {
            this.setState({
                currentIndex: 0,
                select1: true,
                select2: false,
                select3: false,
                select4: false
            });
        }
    }

    private restore(restore: boolean = true) {
        if (restore) {
            this.setState({}, () => {
                this.props.onHide();
            });
        } else {
            this.setState({}, () => {
                if (this.props.onHide) {
                    this.props.onHide(this.state.rootLetters);
                }
            });
        }
    }

    private noop() { }

    render() {
        const divStyle: any = {
            'direction': 'rtl'
        };

        const header = (
            <div style={divStyle}>&nbsp;</div>
        );

        const footer = (
            <div>
                <Button label="Reset" onClick={(e) => this.resetSelection()} className="p-button-text" />
                <Button label="Cancel" onClick={(e) => this.restore()} className="p-button-text" />
                <Button label="OK" onClick={(e) => this.restore(false)} autoFocus />
            </div>
        );

        return (
            <Dialog header={header} footer={footer} onHide={() => this.noop()} visible={this.props.visible} closeOnEscape={false} closable={false}
                onShow={this.show}>
                <div style={divStyle}>
                    <ToggleSelecter value={this.state.rootLetters.firstRadical} index={0} checked={this.state.select1}
                        className="arabicToggleButton ui-button p-button-raised" onChange={this.letterSelected} />&nbsp;
                    <ToggleSelecter value={this.state.rootLetters.secondRadical} index={1} checked={this.state.select2}
                        className="arabicToggleButton ui-button p-button-raised" onChange={this.letterSelected} />&nbsp;
                    <ToggleSelecter value={this.state.rootLetters.thirdRadical} index={2} checked={this.state.select3}
                        className="arabicToggleButton ui-button p-button-raised" onChange={this.letterSelected} />&nbsp;
                    <ToggleSelecter value={this.state.rootLetters.fourthRadical} index={3} checked={this.state.select4}
                        className="arabicToggleButton ui-button p-button-raised" onChange={this.letterSelected} />
                </div>
                <div>&nbsp;</div>
                <div style={divStyle}>
                    <ArabicButton letter={this.letters[0]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[1]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[2]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[3]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[4]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[5]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[6]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[7]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[8]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[9]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[10]} onClick={this.selectLetter} />&nbsp;
                </div>
                <div>&nbsp;</div>
                <div style={divStyle}>
                    <ArabicButton letter={this.letters[11]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[12]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[13]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[14]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[15]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[16]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[17]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[18]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[19]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[20]} onClick={this.selectLetter} />&nbsp;
                </div>
                <div>&nbsp;</div>
                <div style={divStyle}>
                    <ArabicButton letter={this.letters[21]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[22]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[23]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[24]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[25]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[26]} onClick={this.selectLetter} />&nbsp;
                    <ArabicButton letter={this.letters[27]} onClick={this.selectLetter} />
                </div>
            </Dialog>
        );
    }
}