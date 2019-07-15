//  Created by react-native-create-bridge

import React, { Component } from 'react'
import { requireNativeComponent } from 'react-native'

const LibIndy = requireNativeComponent('LibIndy', LibIndyView)

export default class LibIndyView extends Component {
  render () {
    return <LibIndy {...this.props} />
  }
}

LibIndyView.propTypes = {
  exampleProp: React.PropTypes.any
}
