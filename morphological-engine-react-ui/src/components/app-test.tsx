import * as React from 'react';
import ArabicButton from './arabic-button';
import ToggleSelecter from './toggle-selecter';
import { ArabicLetter } from './model/arabic-letter';
import { ArabicLabel } from './model/models';
import Emitter from '../services/event-emitter';

export default class AppTest extends React.Component {

    componentDidMount() {
        Emitter.on('button-clicked', (value: ArabicLabel) => console.log(`Button clicked: ${value.name}`));
        Emitter.on('toggle-state-changed', (newValue) => console.log(newValue));
    }

    componentWillUnmount() {
        Emitter.off('button-clicked')
        Emitter.off('toggle-state-changed');
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
                    <div><ToggleSelecter value={ArabicLetter.MEEM} className="arabicButton ui-button p-button-raised"/></div>
                </div>
            </div>
        );
    }
}