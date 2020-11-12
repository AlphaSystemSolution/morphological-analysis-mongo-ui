import * as React from 'react';
import { ArabicLabel } from './model/models';
import { Button } from 'primereact/button';
import Emitter from '../services/event-emitter';

interface ArabicButtonProps {
    letter: ArabicLabel
}

export default class ArabicButton extends React.Component<ArabicButtonProps, any> {

    handleClick = (e: any) => {
        Emitter.emit('button-clicked', this.props.letter);
    }

    render() {
        return (
            <Button label={this.props.letter.label} className="arabicButton ui-button p-button-raised" onClick={this.handleClick} />
        );
    }
}
