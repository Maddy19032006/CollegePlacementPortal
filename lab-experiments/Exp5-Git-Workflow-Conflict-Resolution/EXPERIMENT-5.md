# 🧪 Experiment 5: GIT Version Control & Methodology
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 4 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To understand the architecture of Distributed Version Control Systems (DVCS) vs Centralized (SVN), execute common Git commands, manage remote repositories on GitHub, implement Gitflow branching methodology, and simulate & resolve a merge conflict.

---

## 2. Theory: SVN (Centralized) vs. GIT (Distributed)
| Feature | Subversion (SVN) | Git |
|---|---|---|
| **Architecture** | Centralized server required for all operations | Distributed; every developer has a full repository clone |
| **Branching** | Slow, heavyweight (copies directory on server) | Instant, lightweight (simple 41-byte pointer to commit) |
| **Offline Work** | Cannot commit or view history without network | Full history, commits, branches, and diffs work offline |
| **Speed** | Network-bound latency | Local disk-speed execution |

---

## 3. Git Architecture & Three Trees
Git manages code across 3 local states before synchronizing with remotes:
```
[ Working Directory ] ──( git add )──► [ Staging Area / Index ] ──( git commit )──► [ Local Repository (HEAD) ]
                                                                                           │
                                                                                    ( git push )
                                                                                           ▼
                                                                                  [ Remote (GitHub) ]
```

---

## 4. Hands-on Git Commands Checklist

```bash
# 1. Initialize repository
git init

# 2. Check current status and tracking
git status

# 3. Stage changes
git add .

# 4. Commit with conventional message
git commit -m "feat(auth): implement BCrypt password hashing"

# 5. Branch management
git branch -a                    # List all local and remote branches
git checkout -b feature/readiness-score  # Create and switch to new branch
git checkout develop             # Switch to integration branch

# 6. Synchronizing with Remote (GitHub)
git remote add origin https://github.com/<username>/college-placement-portal.git
git push -u origin main
git push -u origin develop
```

---

## 5. Simulating & Resolving a Merge Conflict (Viva Practical Demo)

Merge conflicts occur when two branches modify the exact same line of a file differently.

### Step 1: Create Conflicting Branches
```bash
# Ensure we are on develop
git checkout develop

# Branch A modifies README.md line 3
git checkout -b feature/banner-update
# (Change line 3 to: "Welcome to the College Placement Portal v1.1")
git commit -am "feat: update banner text to v1.1"

# Switch back to develop and branch B modifies the same line
git checkout develop
git checkout -b feature/tagline-update
# (Change line 3 to: "Campus Placement Management System 2026")
git commit -am "feat: update tagline for 2026"
```

### Step 2: Trigger Conflict
```bash
git checkout develop
git merge feature/banner-update   # Fast-forward merge succeeds!

git merge feature/tagline-update  # CONFLICT!
# Output:
# Auto-merging README.md
# CONFLICT (content): Merge conflict in README.md
# Automatic merge failed; fix conflicts and then commit the result.
```

### Step 3: Inspect Conflict Markers in File
Open `README.md` to see Git's conflict markers:
```markdown
<<<<<<< HEAD
Welcome to the College Placement Portal v1.1
=======
Campus Placement Management System 2026
>>>>>>> feature/tagline-update
```

### Step 4: Resolve the Conflict
Edit `README.md` to combine the intended changes and delete the conflict markers:
```markdown
Welcome to the College Placement Portal - Campus Placement Management System 2026
```

### Step 5: Mark Resolved & Complete Merge
```bash
git add README.md
git commit -m "fix(merge): resolve banner tagline conflict between branches"
```

---

## 6. Viva Questions & Answers
1. **Q: What is the difference between `git pull` and `git fetch`?**  
   *A: `git fetch` downloads commits and branches from the remote repository without modifying your working branch. `git pull` executes `git fetch` followed immediately by `git merge`.*
2. **Q: What is `git rebase` vs `git merge`?**  
   *A: `git merge` creates a new merge commit combining history. `git rebase` replays your commits sequentially on top of another branch, maintaining a clean, linear commit history.*
