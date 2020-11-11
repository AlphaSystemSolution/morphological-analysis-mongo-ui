import * as React from 'react';
import ArabicButton from './arabic-button';
import { ArabicLetter } from './model/arabic-letter';

export default class AppTest extends React.Component {

    render() {
        return (
            <div>
                <div>
                    <span className="arabicTitle">Arabic Buttons</span>
                    <div><ArabicButton letter={ArabicLetter.LAM} />&nbsp;<ArabicButton letter={ArabicLetter.AIN} />&nbsp;<ArabicButton letter={ArabicLetter.FA} /></div>
                </div>
            </div>
        );
    }
}