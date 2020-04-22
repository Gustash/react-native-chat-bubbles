import React from 'react';
import {Text, SafeAreaView, StyleSheet} from 'react-native';

const Bubble = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Text>I am a bubble yo dude!</Text>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 20,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default Bubble;
