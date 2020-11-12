import * as React from 'react';
import { ToggleButton } from 'primereact/togglebutton';
import { ArabicLabel } from './model/models';
import Emitter from '../services/event-emitter';

interface ToggleSelecterProps {
    value: ArabicLabel
    className: string
}

interface ToggleSelecterState {
    selected: boolean
}

export default class ToggleSelecter extends React.Component<ToggleSelecterProps, ToggleSelecterState> {

    state: ToggleSelecterState = {
        selected: false
    }

    constructor(props: ToggleSelecterProps) {
        super(props);

        this.handleOnChange.bind(this);
    }

    handleOnChange = (event: any) => {
        this.setState((previousState, props) => ({
            selected: event.value
        }));
        Emitter.emit('state-changed', { 'selected': event.value, 'value': this.props.value });
    }

    render() {
        return (
            <ToggleButton onLabel={this.props.value.label} offLabel={this.props.value.label} onChange={this.handleOnChange}
                className={this.props.className} checked={this.state.selected} />
        );
    }
}