SE CONTRACT;

CREATE TABLE CONTRACT.ARTICLE
(
	ID varchar(32) NOT NULL,
	MAIN_TITLE varchar(256) NOT NULL,
	SUB_TITLE varchar(512),
	CATEGORY varchar(32) NOT NULL,
	PUBLISH_TIME timestamp NOT NULL,
	CONTEXT longtext NOT NULL,
	FIX_TOP boolean NOT NULL,
	PRIMARY KEY (ID)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;

CREATE TABLE CONTRACT.ARTICLE_CATEGORY
(
	ID varchar(32) NOT NULL,
	NAME varchar(32) NOT NULL,
	PRI int NOT NULL,
	TYPE varchar(16) NOT NULL,
	PRIMARY KEY (ID)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


