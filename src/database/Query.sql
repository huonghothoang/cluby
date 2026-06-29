CREATE TABLE users (
                       username TEXT PRIMARY KEY,
                       full_name TEXT NOT NULL,
                       email TEXT UNIQUE NOT NULL,
                       phone TEXT,
                       password_hash TEXT NOT NULL,
                       avatar_url TEXT,
                       last_login_at DATETIME,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clubs (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       name TEXT NOT NULL,
                       description TEXT,
                       logo_url TEXT,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_by TEXT REFERENCES users(username)
);

CREATE TABLE departments (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             club_id INTEGER REFERENCES clubs(id),
                             name TEXT NOT NULL,
                             description TEXT,
                             status TEXT DEFAULT 'ACTIVE',
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             created_by TEXT REFERENCES users(username)
);

CREATE TABLE club_members (
                              club_id INTEGER REFERENCES clubs(id),
                              username TEXT REFERENCES users(username),
                              dept_id INTEGER REFERENCES departments(id),
                              role TEXT NOT NULL,
                              status TEXT DEFAULT 'ACTIVE',
                              note TEXT,
                              joined_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_by TEXT REFERENCES users(username),
                              PRIMARY KEY (club_id, username)
);

CREATE TABLE applications (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              club_id INTEGER REFERENCES clubs(id),
                              username TEXT REFERENCES users(username),
                              target_dept_id INTEGER REFERENCES departments(id),
                              intro TEXT,
                              experience TEXT,
                              reason TEXT,
                              status TEXT DEFAULT 'PENDING',
                              reviewer_note TEXT,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              reviewed_by TEXT REFERENCES users(username)
);

CREATE TABLE events (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        club_id INTEGER REFERENCES clubs(id),
                        dept_id INTEGER REFERENCES departments(id),
                        name TEXT NOT NULL,
                        description TEXT,
                        event_date DATE,
                        event_time TEXT,
                        location TEXT,
                        status TEXT DEFAULT 'PENDING',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        created_by TEXT REFERENCES users(username)
);

CREATE TABLE attendances (
                             event_id INTEGER REFERENCES events(id),
                             username TEXT REFERENCES users(username),
                             status TEXT DEFAULT 'PENDING',
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_by TEXT REFERENCES users(username),
                             PRIMARY KEY (event_id, username)
);

CREATE TABLE works (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       event_id INTEGER REFERENCES events(id),
                       dept_id INTEGER REFERENCES departments(id),
                       name TEXT NOT NULL,
                       description TEXT,
                       start_date DATE,
                       deadline DATE,
                       priority TEXT DEFAULT 'MEDIUM',
                       status TEXT DEFAULT 'PENDING',
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_by TEXT REFERENCES users(username)
);

CREATE TABLE tasks (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       work_id INTEGER REFERENCES works(id),
                       username TEXT REFERENCES users(username),
                       name TEXT NOT NULL,
                       priority TEXT DEFAULT 'MEDIUM',
                       status TEXT DEFAULT 'PENDING',
                       product_link TEXT,
                       is_accepted INTEGER DEFAULT 0,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_by TEXT REFERENCES users(username)
);

CREATE TABLE transactions (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              club_id INTEGER REFERENCES clubs(id),
                              event_id INTEGER REFERENCES events(id),
                              trans_type TEXT NOT NULL,
                              amount INTEGER NOT NULL,
                              content TEXT NOT NULL,
                              trans_date DATE,
                              note TEXT,
                              status TEXT DEFAULT 'COMPLETED',
                              cancel_reason TEXT,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              executor_username TEXT REFERENCES users(username)
);

CREATE TABLE notifications (
                               id INTEGER PRIMARY KEY AUTOINCREMENT,
                               club_id INTEGER REFERENCES clubs(id),
                               content TEXT NOT NULL,
                               dot_color TEXT,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TRIGGER trg_UpdateUsersTime
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE username = OLD.username;
END;

CREATE TRIGGER trg_UpdateClubsTime
    BEFORE UPDATE ON clubs
    FOR EACH ROW
BEGIN
    UPDATE clubs SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id;
END;

CREATE TRIGGER trg_UpdateDepartmentsTime
    BEFORE UPDATE ON departments
    FOR EACH ROW
BEGIN
    UPDATE departments SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id;
END;

CREATE TRIGGER trg_UpdateClubMembersTime
    BEFORE UPDATE ON club_members
    FOR EACH ROW
BEGIN
    UPDATE club_members SET updated_at = CURRENT_TIMESTAMP WHERE club_id = OLD.club_id AND username = OLD.username;
END;

CREATE TRIGGER trg_UpdateApplicationsTime
    BEFORE UPDATE ON applications
    FOR EACH ROW
BEGIN
    UPDATE applications SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id;
END;

CREATE TRIGGER trg_UpdateEventsTime
    BEFORE UPDATE ON events
    FOR EACH ROW
BEGIN
    UPDATE events SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id;
END;

CREATE TRIGGER trg_UpdateAttendancesTime
    BEFORE UPDATE ON attendances
    FOR EACH ROW
BEGIN
    UPDATE attendances SET updated_at = CURRENT_TIMESTAMP WHERE event_id = OLD.event_id AND username = OLD.username;
END;

CREATE TRIGGER trg_UpdateWorksTime
    BEFORE UPDATE ON works
    FOR EACH ROW
BEGIN
    UPDATE works SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id;
END;

CREATE TRIGGER trg_UpdateTasksTime
    BEFORE UPDATE ON tasks
    FOR EACH ROW
BEGIN
    UPDATE tasks SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id;
END;

CREATE TRIGGER trg_UpdateTransactionsTime
    BEFORE UPDATE ON transactions
    FOR EACH ROW
BEGIN
    UPDATE transactions SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id;
END;