# 🖥️ Data Virtualization & Vagrant Guide (Lab Experiment 8)

This guide addresses the concepts and practical tasks for **Lab Experiment 8: Data Virtualization**, covering Hypervisor Types, VM lifecycles, and Vagrant automation.

---

## 1. Virtualization Concepts (Viva & Theory)

### Type-1 vs. Type-2 Hypervisors
| Feature | Type-1 (Bare-Metal) | Type-2 (Hosted) |
|---|---|---|
| **Underlying Layer** | Runs directly on bare hardware | Runs on top of a host Operating System |
| **Performance** | High performance, low latency | Moderate overhead due to host OS |
| **Examples** | VMware ESXi, Microsoft Hyper-V, KVM, Xen | Oracle VirtualBox, VMware Workstation |
| **Typical Use Case**| Enterprise data centers, cloud providers (AWS, Azure) | Developer local environments, testing |

### Full Virtualization vs. Para-Virtualization
- **Full Virtualization:** The guest OS is completely unaware it is being virtualized; binary translation or hardware-assisted virtualization (Intel VT-x / AMD-V) trap sensitive instructions.
- **Para-Virtualization:** The guest OS is modified with hypercalls to directly communicate with the hypervisor (e.g. virtio drivers), improving I/O throughput.

---

## 2. Vagrant Commands & Lifecycle

Vagrant automates the management and provisioning of virtual machines (typically using VirtualBox as a Type-2 provider).

| Command | Action |
|---|---|
| `vagrant init [box-name]` | Initializes a new `Vagrantfile` in the current folder |
| `vagrant up` | Creates, boots, and automatically provisions the VM |
| `vagrant status` | Checks the execution state of the VM (running, poweroff, not created) |
| `vagrant ssh` | Connects to the guest Linux machine via secure shell |
| `vagrant suspend` | Pauses the VM, saving current RAM state to disk |
| `vagrant resume` | Resumes a previously suspended VM |
| `vagrant reload` | Reboots the VM and applies any new `Vagrantfile` configurations |
| `vagrant halt` | Gracefully shuts down the VM operating system |
| `vagrant destroy -f` | Completely deletes the VM disk image and releases all resources |

---

## 3. Running the Placement Portal with Vagrant

1. Install **VirtualBox** and **Vagrant** on your machine.
2. In the project root, start the virtual environment:
   ```bash
   vagrant up
   ```
3. Vagrant will automatically:
   - Download the official `ubuntu/jammy64` box.
   - Allocate 2 vCPUs and 2048 MB RAM.
   - Set up port forwarding (`8081` $\rightarrow$ `8081`).
   - Run the inline shell provisioner to install Java 21, MySQL, Maven, build the project, and start the service.
4. Access the portal from your host browser: `http://localhost:8081`.
5. SSH into the machine if needed:
   ```bash
   vagrant ssh
   ```
