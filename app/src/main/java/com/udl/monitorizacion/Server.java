package com.udl.monitorizacion;

/**********
 *
 **********/
public class Server {
    String server;
    String cpu;
    String ram;

    public Server(String server, String cpu, String ram) {
        this.server = server;
        this.cpu = cpu;
        this.ram = ram;
    }

    public String getServer() {

        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }
}
