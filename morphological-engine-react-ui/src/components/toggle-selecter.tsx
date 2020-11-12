import * as React from 'react';
import { ToggleButton } from 'primereact/togglebutton';
import { ArabicLabel } from './model/models';
import Emitter from '../services/event-emitter';

interface ToggleSelecterProps {
    value: ArabicLabel
    className: string
    selected?: boolean
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
        this.state = {
            selected: this.props.selected ? true : false
        }
    }

    handleOnChange = (event: any) => {
        this.setState((previousState, props) => ({
            selected: event.value
        }));
        Emitter.emit('toggle-state-changed', { 'selected': event.value, 'value': this.props.value });
    }

    render() {
        return (
            <ToggleButton onLabel={this.props.value.label} offLabel={this.props.value.label} onChange={this.handleOnChange}
                className={this.props.className} checked={this.state.selected} />
        );
    }
}