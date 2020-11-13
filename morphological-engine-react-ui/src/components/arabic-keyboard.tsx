import * as React from 'react';
import { OverlayPanel } from 'primereact/overlaypanel';
import { ArabicLetter } from './model/arabic-letter';
import ToggleSelecter from './toggle-selecter';
import { RootLetters } from './model/root-letters';
import ArabicButton from './arabic-button';
import { Button } from 'primereact/button';
import Emitter from '../services/event-emitter';

interface Props {
}

interface State {
    rootLetters: RootLetters
    currentIndex: number;
}

export default class ArabicKeyboard extends React.Component<Props, State> {

    op: any = React.createRef();
    toggle1: any = React.createRef();
    toggle2: any = React.createRef();
    toggle3: any = React.createRef();
    toggle4: any = React.createRef();

    constructor(props: any) {
        super(props);

        this.state = {
            rootLetters: new RootLetters(),
            currentIndex: 0
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

    show(e: any) {
        this.op.toggle(e);
    }

    private resetSelection(): void {
        this.unselectCurrent();
        this.setState({
            rootLetters: new RootLetters(),
            currentIndex: 0
        }, () => {
            this.toggle1.select(true);
        })
    }

    private selectLetter(letter: ArabicLetter) {
        const rl = this.state.rootLetters;
        switch (this.state.currentIndex) {
            case 0:
                this.setState({
                    rootLetters: new RootLetters(letter, rl.secondRadical, rl.thirdRadical, rl.fourthRadical),
                    currentIndex: 1
                }, () => {
                    this.toggle1.select(false);
                    this.toggle2.select(true);
                });
                break;
            case 1:
                this.setState({
                    rootLetters: new RootLetters(rl.firstRadical, letter, rl.thirdRadical, rl.fourthRadical),
                    currentIndex: 2
                }, () => {
                    this.toggle2.select(false);
                    this.toggle3.select(true);
                });
                break;
            case 2:
                this.setState({
                    rootLetters: new RootLetters(rl.firstRadical, rl.secondRadical, letter, rl.fourthRadical),
                    currentIndex: 3
                }, () => {
                    this.toggle3.select(false);
                    this.toggle4.select(true);
                });
                break;
            case 3:
                this.setState({
                    rootLetters: new RootLetters(rl.firstRadical, rl.secondRadical, rl.thirdRadical, letter),
                    currentIndex: 0
                }, () => {
                    this.toggle4.select(false);
                    this.toggle1.select(true);
                });
                break;
        }
    }

    private letterSelected(payload: any) {
        const selected: boolean = payload.selected;
        const index: number = payload.index;
        if (selected) {
            this.unselectCurrent();
            switch (index) {
                case 0:
                    this.setState({
                        currentIndex: 0
                    })
                    break;
                case 1:
                    this.setState({
                        currentIndex: 1
                    })
                    break;
                case 2:
                    this.setState({
                        currentIndex: 2
                    })
                    break;
                case 3:
                    this.setState({
                        currentIndex: 3
                    })
                    break;
            }
        } else {
            this.setState({
                currentIndex: 0
            }, () => {
                this.toggle1.select(true);
            });
        }
    }

    private unselectCurrent() {
        switch (this.state.currentIndex) {
            case 0:
                this.toggle1.select(false);
                break;
            case 1:
                this.toggle2.select(false);
                break;
            case 2:
                this.toggle3.select(false);
                break;
            case 3:
                this.toggle4.select(false);
                break;
        }
    }

    render() {
        const divStyle: any = {
            'direction': 'rtl'
        };

        return (
            <OverlayPanel ref={(el) => this.op = el} id="overlay_panel" showCloseIcon dismissable={false}>
                <div style={divStyle}>
                    <ToggleSelecter ref={(el) => this.toggle1 = el} value={this.state.rootLetters.firstRadical} index={0}
                        className="arabicToggleButton ui-button p-button-raised" selected={true} />&nbsp;
                    <ToggleSelecter ref={(el) => this.toggle2 = el} value={this.state.rootLetters.secondRadical} index={1}
                        className="arabicToggleButton ui-button p-button-raised" />&nbsp;
                    <ToggleSelecter ref={(el) => this.toggle3 = el} value={this.state.rootLetters.thirdRadical} index={2}
                        className="arabicToggleButton ui-button p-button-raised" />&nbsp;
                    <ToggleSelecter ref={(el) => this.toggle4 = el} value={this.state.rootLetters.fourthRadical} index={3}
                        className="arabicToggleButton ui-button p-button-raised" />
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
                    <ArabicButton letter={this.letters[27]} />&nbsp;
                    <Button label="Reset" className="arabicButton ui-button p-button-text p-button-raised" onClick={(e) => this.resetSelection()} />
                </div>
            </OverlayPanel>
        );
    }
}