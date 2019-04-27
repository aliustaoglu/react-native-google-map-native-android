
import React, {Component} from 'react';
import {DeviceEventEmitter, StyleSheet, requireNativeComponent, UIManager, findNodeHandle} from 'react-native';
import cities from './us_cities.json'

const mapRef = React.createRef()
const GMap = requireNativeComponent('GMap')

export default class App extends Component {
  constructor(props){
    super(props)
    this.state = {}
    this.onMapReady = this.onMapReady.bind(this)
  }

  onMapReady(){
    cities.map((city, i) => {
      UIManager.dispatchViewManagerCommand(this.mapViewHandle, 0, [
        'marker' + 0,
        city.latitude,
        city.longitude,
        city.city,
        city.state,
        city.population
      ]);
    });
  }

  componentDidMount(){
    this.mapViewHandle = findNodeHandle(mapRef.current);
    DeviceEventEmitter.addListener('onMapReady', this.onMapReady);
  }

  render() {
    return (
      <GMap ref={mapRef} style={StyleSheet.absoluteFillObject} />
    );
  }
}
