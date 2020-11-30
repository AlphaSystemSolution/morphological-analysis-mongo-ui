import * as React from 'react';
import { ToggleButton } from 'primereact/togglebutton';
import { ArabicLabel } from './model/arabic-label';

interface Props {
    value: ArabicLabel
    index: number
    className: string
    checked: boolean
    userKey?: any
    onChange(payload: any): void
}

interface State { }

export class ToggleSelecter extends React.Component<Props, State> {

    state: State = {}

    handleOnChange = (event: any) => {
        this.props.onChange({ 'selected': event.value, 'value': this.props.value, 'index': this.props.index, 'userKey': this.props.userKey });
    }

    render() {
        return (
            <ToggleButton onLabel={this.props.value.label} offLabel={this.props.value.label} onChange={this.handleOnChange}
                className={this.props.className} checked={this.props.checked} />
        );
    }
}