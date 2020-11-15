import * as React from 'react';
import { Dialog } from 'primereact/dialog';
import ToggleSelecter from './toggle-selecter';
import { Button } from 'primereact/button';
import { ArabicLetter } from './model/arabic-letter';
import { RootLetters } from './model/root-letters';
import ArabicButton from './arabic-button';
import Emitter from '../services/event-emitter';

interface Props {
    initialLetters?: RootLetters
    onHide?(rootLetters: RootLetters): void
}

interface State {
    rootLetters: RootLetters
    prevLetters: RootLetters
    currentIndex: number
    visible: boolean
    select1: boolean
    select2: boolean
    select3: boolean
    select4: boolean
}

export default class ArabicKeyboard extends React.Component<Props, State> {

    constructor(props: any) {
        super(props);

        this.state = {
            rootLetters: this.props.initialLetters ? this.props.initialLetters : new RootLetters(),
            prevLetters: this.props.initialLetters ? this.props.initialLetters : new RootLetters(),
            currentIndex: 0,
            visible: false,
            select1: true,
            select2: false,
            select3: false,
            select4: false
        }
    }

    componentDidMount() {
        Emitter.on('button-clicked', (value: ArabicLetter) => this.selectLetter(value));
        Emitter.on('toggle-state-changed', (newValue) => this.letterSelected(newValue));
    }

    componentWillUnmount() {
        Emitter.off('button-clicked')
        Emitter.off('toggle-state-changed');
    }

    get letters(): ArabicLetter[] {
        return ArabicLetter.arabicLetters;
    }

    public show(): void {
        this.setState({
            prevLetters: this.state.rootLetters,
            visible: true,
            currentIndex: 0,
            select1: true,
            select2: false,
            select3: false,
            select4: false
        })
    }

    private resetSelection(): void {
        this.setState({
            rootLetters: this.state.prevLetters,
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
            this.setState({
                rootLetters: this.state.prevLetters,
                visible: false
            });
        } else {
            this.setState({
                visible: false
            });
        }
    }

    private hide() {
        this.setState({}, () => {
            if (this.props.onHide) {
                this.props.onHide(this.state.rootLetters);
            }
        });
    }

    render() {
        const divStyle: any = {
            'direction': 'rtl'
        };

        const header = (
            <div style={divStyle}>&nbsp;</div>
        );

        const footer = (
            <div>
                <Button label="Reset" onClick={(e) => this.resetSelection()} />
                <Button label="Cancel" onClick={(e) => this.restore()} className="p-button-text" />
                <Button label="OK" onClick={(e) => this.restore(false)} autoFocus />
            </div>
        );

        return (
            <Dialog header={header} footer={footer} onHide={() => this.hide()} visible={this.state.visible} closeOnEscape={false} closable={false}>
                <div style={divStyle}>
                    <ToggleSelecter value={this.state.rootLetters.firstRadical} index={0}
                        className="arabicToggleButton ui-button p-button-raised" selected={this.state.select1} />&nbsp;
                    <ToggleSelecter value={this.state.rootLetters.secondRadical} index={1}
                        className="arabicToggleButton ui-button p-button-raised" selected={this.state.select2} />&nbsp;
                    <ToggleSelecter value={this.state.rootLetters.thirdRadical} index={2}
                        className="arabicToggleButton ui-button p-button-raised" selected={this.state.select3} />&nbsp;
                    <ToggleSelecter value={this.state.rootLetters.fourthRadical} index={3}
                        className="arabicToggleButton ui-button p-button-raised" selected={this.state.select4} />
                </div>
                <div>&nbsp;</div>
                <div style={divStyle}>
                    <ArabicButton letter={this.letters[0]} />&nbsp;
                    <ArabicButton letter={this.letters[1]} />&nbsp;
                    <ArabicButton letter={this.letters[2]} />&nbsp;
                    <ArabicButton letter={this.letters[3]} />&nbsp;
                    <ArabicButton letter={this.letters[4]} />&nbsp;
                    <ArabicButton letter={this.letters[5]} />&nbsp;
                    <ArabicButton letter={this.letters[6]} />&nbsp;
                    <ArabicButton letter={this.letters[7]} />&nbsp;
                    <ArabicButton letter={this.letters[8]} />&nbsp;
                    <ArabicButton letter={this.letters[9]} />&nbsp;
                    <ArabicButton letter={this.letters[10]} />&nbsp;
                </div>
                <div>&nbsp;</div>
                <div style={divStyle}>
                    <ArabicButton letter={this.letters[11]} />&nbsp;
                    <ArabicButton letter={this.letters[12]} />&nbsp;
                    <ArabicButton letter={this.letters[13]} />&nbsp;
                    <ArabicButton letter={this.letters[14]} />&nbsp;
                    <ArabicButton letter={this.letters[15]} />&nbsp;
                    <ArabicButton letter={this.letters[16]} />&nbsp;
                    <ArabicButton letter={this.letters[17]} />&nbsp;
                    <ArabicButton letter={this.letters[18]} />&nbsp;
                    <ArabicButton letter={this.letters[19]} />&nbsp;
                    <ArabicButton letter={this.letters[20]} />&nbsp;
                </div>
                <div>&nbsp;</div>
                <div style={divStyle}>
                    <ArabicButton letter={this.letters[21]} />&nbsp;
                    <ArabicButton letter={this.letters[22]} />&nbsp;
                    <ArabicButton letter={this.letters[23]} />&nbsp;
                    <ArabicButton letter={this.letters[24]} />&nbsp;
                    <ArabicButton letter={this.letters[25]} />&nbsp;
                    <ArabicButton letter={this.letters[26]} />&nbsp;
                    <ArabicButton letter={this.letters[27]} />
                </div>
            </Dialog>
        );
    }
}