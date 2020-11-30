import * as React from 'react';
import { ArabicLabel } from './model/arabic-label';
import { Button } from 'primereact/button';

interface ArabicButtonProps {
    letter: ArabicLabel
    onClick(letter: ArabicLabel): void
}

export default class ArabicButton extends React.Component<ArabicButtonProps, any> {

    handleClick = (e: any) => {
        this.props.onClick(this.props.letter)
    }

    render() {
        return (
            <Button label={this.props.letter.label} className="arabicButton ui-button p-button-text p-button-raised" onClick={this.handleClick} />
        );
    }
}
