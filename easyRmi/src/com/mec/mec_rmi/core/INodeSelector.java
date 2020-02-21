package com.mec.mec_rmi.core;

public interface INodeSelector {
	INode getNode();
	void connectFaliure(INode node);
	int getDelayTime();
}
