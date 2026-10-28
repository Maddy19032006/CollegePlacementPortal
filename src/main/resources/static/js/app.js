/* Shared helpers for all pages. Uses fetch against the REST API with session cookies. */

const API_BASE = '/api';

/** Wrapper around fetch that throws on !ok using the ApiError body message. */
async function api(path, options = {}) {
    const response = await fetch(API_BASE + path, {
        credentials: 'same-origin',
        headers: options.body ? { 'Content-Type': 'application/json' } : {},
        ...options,
    });
    if (response.status === 401 && path !== '/auth/login') {
        window.location.href = '/login.html';
        throw new Error('Please login to continue');
    }
    const data = response.status === 204 ? null : await response.json().catch(() => null);
    if (!response.ok) {
        const message = data && data.message ? data.message : `Request failed (${response.status})`;
        throw new Error(message);
    }
    return data;
}

/** Escapes API/user content before it is added to an HTML template. */
function escapeHtml(value) {
    return String(value ?? '')
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#039;');
}

/** Shows a Bootstrap alert inside #alert-placeholder. */
function showAlert(message, type = 'danger') {
    const placeholder = document.getElementById('alert-placeholder');
    if (!placeholder) return;
    placeholder.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${escapeHtml(message)}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>`;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function clearAlert() {
    const placeholder = document.getElementById('alert-placeholder');
    if (placeholder) placeholder.innerHTML = '';
}

/** Fetches the logged-in user; redirects to login if not authenticated. */
async function requireAuth() {
    try {
        const user = await api('/auth/me');
        sessionStorage.setItem('placement-user', JSON.stringify(user));
        return user;
    } catch (error) {
        window.location.href = '/login.html';
        throw error;
    }
}

/** Ensures the logged-in user has one of the given roles, else redirects home. */
function requireRole(user, ...roles) {
    if (!roles.includes(user.role)) {
        window.location.href = '/index.html';
        throw new Error('You do not have permission to view this page');
    }
}

function statusBadge(status) {
    return `<span class="badge status-${escapeHtml(status)}">${escapeHtml(status).replaceAll('_', ' ')}</span>`;
}

function formatDate(value) {
    if (!value) return '-';
    return new Date(value).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}

function navbar(active) {
    let user = null;
    try { user = JSON.parse(sessionStorage.getItem('placement-user')); } catch (_) { /* rendered before login */ }
    const role = user?.role;
    const linksByRole = {
        STUDENT: [
            ['student-dashboard.html', 'Overview', 'student-dashboard'],
            ['student-profile.html', 'My profile', 'student-profile'],
            ['student-preparation.html', 'Readiness', 'student-preparation'],
            ['companies.html', 'Opportunities', 'companies'],
            ['student-applications.html', 'Applications', 'student-applications'],
        ],
        PLACEMENT_OFFICER: [
            ['officer-dashboard.html', 'Overview', 'officer-dashboard'],
            ['officer-companies.html', 'Job offers', 'officer-companies'],
            ['officer-students.html', 'Students', 'officer-students'],
            ['officer-applications.html', 'Applications', 'officer-applications'],
        ],
        ADMIN: [
            ['admin-reports.html', 'Analytics', 'admin-reports'],
            ['admin-users.html', 'Users', 'admin-users'],
            ['officer-companies.html', 'Job offers', 'officer-companies'],
            ['officer-applications.html', 'Applications', 'officer-applications'],
        ],
    };
    const items = (linksByRole[role] || []).map(([href, label, key]) =>
        `<a class="nav-link ${key === active ? 'active' : ''}" href="/${href}">${label}</a>`).join('');
    const firstName = escapeHtml((user?.fullName || '').trim().split(/\s+/)[0] || 'Account');
    return `
    <nav class="navbar navbar-expand-lg app-navbar sticky-top">
        <div class="container">
            <a class="navbar-brand" href="/index.html"><span class="brand-mark">P</span> PlacePilot</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#app-navigation" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="app-navigation">
            <div class="navbar-nav ms-auto align-items-lg-center gap-lg-1">
                ${items}
                <span class="nav-user d-none d-lg-inline">Hi, ${firstName}</span>
                <button class="btn btn-light btn-sm ms-lg-2" onclick="logout()">Sign out</button>
            </div>
            </div>
        </div>
    </nav>`;
}

async function logout() {
    try { await api('/logout', { method: 'POST' }); } catch (ignored) { /* already logged out */ }
    sessionStorage.removeItem('placement-user');
    window.location.href = '/login.html';
}
