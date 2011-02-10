--DROP TABLE IF EXISTS attlist;

CREATE TEMP TABLE attlist
(
	tagtype_id TEXT,
	tag_id INTEGER,
	tagname TEXT,
	tagidentifier TEXT,
	attributetype_id TEXT,
    attname TEXT,
    attvalue TEXT
);

INSERT INTO attlist(tagname,attname,attvalue)
SELECT * 
FROM ${attlist};

-- add the derived column names
UPDATE attlist
SET
	tagtype_id='${tagtype_id}',
	tagidentifier='${tagtype_id}' || ':' || attlist.tagname,
	attributetype_id='${tagtype_id}' || ':' || attlist.attname;

--CREATE INDEX index_attlist_tagtype_id ON attlist(tagtype_id);
--CREATE INDEX index_attlist_tag_id ON attlist(tag_id);
--CREATE INDEX index_attlist_tagname ON attlist(tagname);
--CREATE INDEX index_attlist_attributetype_id ON attlist(attributetype_id);
--CREATE INDEX index_attlist_attname ON attlist(attname);
--CREATE INDEX index_attlist_attvalue ON attlist(attvalue);

--ALTER TABLE attributes DROP CONSTRAINT attributes_tag_id_fkey;
--ALTER TABLE attributes DROP CONSTRAINT attributes_attributetype_id_fkey;

-- add tag IDs for existing tags
UPDATE attlist
SET tag_id=tags.id
FROM tags
WHERE tags.name=attlist.tagname;

-- create any new tags
INSERT INTO tags (tagtype_id,tagidentifier,identifier,name)
SELECT distinct tagtype_id, tagidentifier, attlist.tagname, attlist.tagname
FROM attlist
WHERE tag_id is NULL;

-- look up tag IDs for any newly added tags
UPDATE attlist
SET tag_id=tags.id
FROM tags
WHERE tags.name=attlist.tagname
AND attlist.tag_id is null;

-- update attribute values for any attributes that are already defined
UPDATE attributes
SET value=attlist.attvalue
FROM attlist
WHERE attributes.tag_id=attlist.tag_id
AND attributes.attributetype_id=attlist.attributetype_id;

-- remove any attributes from attlist that were already updated
DELETE FROM attlist
WHERE (tag_id,attributetype_id) in (select tag_id, attributetype_id FROM attributes);

-- insert the remaining rows as-is into the attributes table
INSERT INTO attributes(tag_id, attributetype_id, value)
SELECT tag_id, attributetype_id, attvalue
FROM attlist;

--ALTER TABLE attributes ADD FOREIGN KEY (tag_id) REFERENCES tags(id);
--ALTER TABLE attributes ADD FOREIGN KEY (attributetype_id) REFERENCES attributetypes(id);

DROP TABLE attlist;
