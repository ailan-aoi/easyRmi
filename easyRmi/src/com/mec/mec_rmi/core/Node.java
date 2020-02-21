package com.mec.mec_rmi.core;

import java.io.Serializable;

import com.mec.mec_rmi.core.INode;

public class Node implements INode, Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7861006415563354661L;
	private int port;
    private String ip;
    private String serviceId;
    
    public Node() {}

    public Node(int port, String ip) {
        setPort(port);
        setIp(ip);
    }
    
    public Node(int port, String ip, String serviceId) {
        setPort(port);
        setIp(ip);
        setServiceId(serviceId);
    }

    private void setServiceId(String serviceId) {
    	this.serviceId = serviceId;
	}

	void setPort(int port) {
        if (port < 10000 || port > 65535) {
        	port = 54188;
            return;
        }

        this.port = port;
    }

    void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getIp() {
        return ip;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [port=" + port + ", ip=" + ip + "]";
	}

	public String getServiceId() {
		return serviceId;
	}
    
}
