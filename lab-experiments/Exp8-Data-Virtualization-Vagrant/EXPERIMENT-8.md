# 🧪 Experiment 8: Data Virtualization & Vagrant
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 3 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To examine server-based virtualization and hypervisor architectures (Type 1 vs Type 2), understand full virtualization vs para-virtualization lifecycles, configure virtual machine networking, and automate VM creation and provisioning using Vagrant.

---

## 2. Theory: Virtualization Architectures

### A. Type 1 vs. Type 2 Hypervisors
```
Type 1 (Bare-Metal):
[ Hardware ] ──► [ Hypervisor (ESXi, KVM, Xen) ] ──► [ Guest OS 1 ] / [ Guest OS 2 ]

Type 2 (Hosted):
[ Hardware ] ──► [ Host OS (Windows/Linux) ] ──► [ Hypervisor (VirtualBox) ] ──► [ Guest OS (Ubuntu) ]
```

### B. Full Virtualization vs. Para-Virtualization
- **Full Virtualization:** The guest OS is unmodified and unaware of the virtualization layer. All CPU and I/O instructions are intercepted by hardware-assisted virtualization (Intel VT-x / AMD-V).
- **Para-Virtualization:** The guest OS is modified with customized device drivers (e.g., VirtIO) to make direct hypercalls to the hypervisor, drastically reducing I/O overhead.

---

## 3. Vagrant Automation (`Vagrantfile`)
In this project, [`Vagrantfile`](../../Vagrantfile) automates the creation of a full Ubuntu 22.04 LTS development box:

```ruby
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  config.vm.hostname = "placement-portal-dev"

  # Port Forwarding: Access application on host via http://localhost:8081
  config.vm.network "forwarded_port", guest: 8081, host: 8081, auto_correct: true

  # VirtualBox Provider Resource Allocation
  config.vm.provider "virtualbox" do |vb|
    vb.name = "CollegePlacementPortal-VM"
    vb.memory = "2048" # 2GB RAM
    vb.cpus = 2        # 2 vCPUs
  end

  # Automated Shell Provisioning
  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update -y
    sudo apt-get install -y openjdk-21-jdk maven mysql-server
    # Setup DB, compile project and start background service
  SHELL
end
```

---

## 4. Hands-on Vagrant Lifecycle Commands (For Lab Exam)

```bash
# 1. Initialize a new Vagrantfile (already provided in project root)
vagrant init ubuntu/jammy64

# 2. Boot and provision the virtual machine
vagrant up

# 3. Check VM execution state (running, poweroff, etc.)
vagrant status

# 4. SSH into the guest virtual machine
vagrant ssh

# Inside VM: verify Java and MySQL
java -version
mysql --version
exit

# 5. Access application from host browser
# http://localhost:8081

# 6. Pause VM (suspend execution state to disk)
vagrant suspend

# 7. Resume suspended VM
vagrant resume

# 8. Gracefully shut down the VM
vagrant halt

# 9. Destroy VM and release host disk space
vagrant destroy -f
```

---

## 5. Viva Questions & Answers
1. **Q: What is a Vagrant Box?**  
   *A: A Vagrant Box is a pre-packaged, base virtual machine image (similar to a Docker image) that serves as the foundation for creating reproducible VM environments.*
2. **Q: How does Vagrant port forwarding work?**  
   *A: It maps a TCP port on the guest virtual machine (e.g. port 8081) to an open port on the host machine (e.g. port 8081), routing browser requests through the hypervisor NAT engine.*
