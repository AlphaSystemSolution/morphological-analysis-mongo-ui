import * as React from 'react';
import ArabicButton from './arabic-button';
import ToggleSelecter from './toggle-selecter';
import ArabicKeyboard from './arabic-keyboard';
import { ArabicLetter } from './model/arabic-letter';
//import { ArabicLabel } from './model/models';
//import Emitter from '../services/event-emitter';
import { Button } from 'primereact/button';

export default class AppTest extends React.Component {

    keyboardRef: any = React.createRef();

    componentDidMount() {
        //Emitter.on('button-clicked', (value: ArabicLabel) => console.log(`Button clicked: ${value.name}`));
        //Emitter.on('toggle-state-changed', (newValue) => console.log(newValue));
    }

    componentWillUnmount() {
        //Emitter.off('button-clicked')
        //Emitter.off('toggle-state-changed');
    }

    render() {
        return (
            <div>
                <div>
                    <span className="arabicTitle">Arabic Buttons</span>
                    <div><ArabicButton letter={ArabicLetter.LAM} />&nbsp;<ArabicButton letter={ArabicLetter.AIN} />&nbsp;<ArabicButton letter={ArabicLetter.FA} /></div>
                </div>
                <div>&nbsp;</div>
                <div>
                    <span className="arabicTitle">Toggle selecter</span>
                    <div><ToggleSelecter value={ArabicLetter.MEEM} index={0} className="arabicButton ui-button p-button-raised"/></div>
                </div>
                <div>&nbsp;</div>
                <div>
                    <ArabicKeyboard ref={(el) => this.keyboardRef = el}/>
                    <span className="arabicTitle">Keyboard</span>
                    <div><Button label="Show keyboard" onClick={(e) => this.keyboardRef.show(e)} aria-haspopup aria-controls="overlay_panel"/></div>
                </div>
            </div>
        );
    }
}