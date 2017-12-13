-- Devices:
CREATE TABLE devices(
  id serial UNIQUE,
  device_id VARCHAR(50) NOT NULL UNIQUE,
  date_created TIMESTAMP DEFAULT NOW()
);

-- Devices' current frequencies:
CREATE TABLE device_navigation_params(
  id serial UNIQUE,
  device_id VARCHAR(50) NOT NULL,
  type INTEGER NOT NULL,
  frequency INTEGER,
  duration INTEGER
);

-- Messages (both Morse code and frequencies):
CREATE TABLE messages(
  id serial UNIQUE,
  message_id VARCHAR(500) NOT NULL,
  device_id VARCHAR(50) NOT NULL,
  command INTEGER NOT NULL,
  frequency INTEGER NOT NULL,
  duration INTEGER NOT NULL,
  message_text TEXT,
  publish_date timestamp NOT NULL,
  status INTEGER DEFAULT 1, -- !!! needs adding to documentation and diagram
  status_date timestamp -- !!! needs adding to documentation and diagram
);

-- DeviceDTO status logs:
CREATE TABLE connection_logs(
  id serial UNIQUE,
  device_id VARCHAR(50) NOT NULL,
  status INTEGER NOT NULL,
  status_date timestamp DEFAULT NOW()
);

-- Faulty messages' logs
CREATE TABLE faulty_message_logs(
  id serial UNIQUE,
  message TEXT,
  dtstamp TIMESTAMP DEFAULT NOW()
);