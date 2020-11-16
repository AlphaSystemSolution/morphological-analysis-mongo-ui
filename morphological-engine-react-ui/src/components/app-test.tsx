import * as React from 'react';
import ArabicKeyboard from './arabic-keyboard';
import { ArabicLetter } from './model/arabic-letter';
// import Emitter from '../services/event-emitter';
import { Button } from 'primereact/button';
import { RootLetters } from './model/root-letters';
import InputTable from './input-table';
import { InputData } from './model/models';
import { NamedTemplate } from './model/named-template';

export default class AppTest extends React.Component {

    keyboardRef: any = React.createRef();

    componentDidMount() {
        //Emitter.on('button-clicked', (value: ArabicLabel) => console.log(`Button clicked: ${value.name}`));
        // Emitter.on('letters-selected', (newValue) => console.log(JSON.stringify(newValue)));
    }

    componentWillUnmount() {
        //Emitter.off('button-clicked')
        // Emitter.off('letters-selected');
    }

    render() {
        const rootLetters: RootLetters = new RootLetters(ArabicLetter.SEEN, ArabicLetter.LAM, ArabicLetter.MEEM)
        const data = [new InputData(rootLetters, NamedTemplate.FORM_IV_TEMPLATE), new InputData()]
        return (
            <div>
                <div>

                    <ArabicKeyboard ref={(el) => this.keyboardRef = el} initialLetters={rootLetters} onHide={(e) => console.log(`>>>>: ${JSON.stringify(e)}`)} />
                    <span className="arabicTitle">Keyboard</span>
                    <div><Button label="Show keyboard" onClick={(e) => this.keyboardRef.show()} aria-haspopup aria-controls="overlay_panel" /></div>
                </div>
                <div>&nbsp;</div>
                <div>
                    <InputTable initialData={data} />
                </div>
            </div>
        );
    }
}