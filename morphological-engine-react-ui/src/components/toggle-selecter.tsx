import * as React from 'react';
import { ToggleButton } from 'primereact/togglebutton';
import { ArabicLabel } from './model/models';
import Emitter from '../services/event-emitter';

interface Props {
    value: ArabicLabel
    index: number
    className: string
    selected: boolean
}

interface State {
    selected: boolean
}

export default class ToggleSelecter extends React.Component<Props, State> {

    state: State = {
        selected: this.props.selected ? true : false
    }

    handleOnChange = (event: any) => {
        this.setState({ selected: event.value }, () => {
            Emitter.emit('toggle-state-changed', { 'selected': this.state.selected, 'value': this.props.value, 'index': this.props.index });
        });
    }

    render() {
        return (
            <ToggleButton onLabel={this.props.value.label} offLabel={this.props.value.label} onChange={this.handleOnChange}
                className={this.props.className} checked={this.props.selected} />
        );
    }
}