package com.elastica.beatle.detect.dto;

import java.util.List;

public class Groups
{
private int window;

private List<Steps> steps;

private int threshold;







public int getWindow() {
	return window;
}







public void setWindow(int window) {
	this.window = window;
}







public List<Steps> getSteps() {
	return steps;
}







public void setSteps(List<Steps> steps) {
	this.steps = steps;
}







public int getThreshold() {
	return threshold;
}







public void setThreshold(int threshold) {
	this.threshold = threshold;
}







@Override
public String toString()
{
return "ClassPojo [window = "+window+", steps = "+steps+", threshold = "+threshold+"]";
}
}

