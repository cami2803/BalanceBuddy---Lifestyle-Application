import React from 'react';
import { View, Text, Dimensions } from 'react-native';
import { AnimatedCircularProgress } from 'react-native-circular-progress';

const { width } = Dimensions.get('window');

const CircularProgress = ({ percentage }) => {
    return (
        <View style={{ justifyContent: 'center', alignItems: 'center', flex: 1 }}>
            <AnimatedCircularProgress
                size={width * 0.6} // 60% of the screen width
                width={15}
                fill={percentage}
                tintColor="#0c752b"
                backgroundColor="#e0afa0"
                rotation={0}
            >
                {fill => (
                    <Text style={{ fontSize: 24, color: '#573d3c', fontWeight: 'bold' }}>
                        {`${Math.round(fill)}%`}
                    </Text>
                )}
            </AnimatedCircularProgress>
        </View>
    );
};

export default CircularProgress;
