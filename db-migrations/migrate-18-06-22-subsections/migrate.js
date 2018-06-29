var pg = require('pg');

(async function(){
  var clientOld = new pg.Client("postgres://postgres:iristra@localhost:5432/c1db-bk");
  clientOld.connect();

  var clientNew = new pg.Client("postgres://postgres:iristra@localhost:5432/c1db");
  clientNew.connect();


  /* set top model sections */
  /* get all top model sections */
  // const topSectionsResult = await clientOld.query("SELECT id, top_model_section_id FROM initiatives");
  //
  // for (var ixTopModelSection = 0; ixTopModelSection < topSectionsResult.rows.length; ixTopModelSection++) {
  //   let row = topSectionsResult.rows[ixTopModelSection];
  //   console.log('initiative id:           ' + row.id);
  //   console.log('top_model_section_id id: ' + row.top_model_section_id);
  //
  //   const topSubsectionResult = await clientNew.query("SELECT id FROM model_subsections WHERE section_id = '" + row.top_model_section_id + "'")
  //
  //   let row2 = topSubsectionResult.rows[0];
  //   console.log('subsection id:           ' + row2);
  //
  //   await clientNew.query(
  //     "UPDATE initiatives inita " +
  //     "SET top_model_subsection_id = '" + row2.id + "' " +
  // }

  /* set before and after elements from order column */

  /* get one occurrence of all parent sections in new DB */
  const subsectionsResult = await clientNew.query(
    "SELECT DISTINCT parent_section_id FROM model_subsections WHERE parent_section_id IS NOT NULL");

  for (var ixInSubsections = 0; ixInSubsections < subsectionsResult.rows.length; ixInSubsections++) {
    let subsectionRow = subsectionsResult.rows[ixInSubsections]
    console.log('parent_section_id id: ' + subsectionRow.parent_section_id)

    /* get the number of card additions in old DB */
    var maxOrdersResult = await clientOld.query(
      "SELECT model_section_id, MAX(subsections_order) " +
      "FROM model_sections_subsections " +
      "WHERE model_section_id = '" + subsectionRow.parent_section_id + "' " +
      "GROUP BY model_section_id");

    if (maxOrdersResult.rows.length > 0) {
      /* update the before and after reference if more than one subsection */
      let nSubsections = maxOrdersResult.rows[0].max + 1
      console.log('nSubsections: ' + nSubsections)

      for (var ixOfSubsec = 1; ixOfSubsec < nSubsections; ixOfSubsec++) {

        console.log('updating subsection ' + (ixOfSubsec + 1))

        let thisSecIdResult = await clientOld.query(
          "SELECT subsections_id " +
          "FROM model_sections_subsections sec_subsec " +
          "WHERE sec_subsec.subsections_order = " + (ixOfSubsec - 1) + " " +
          "AND sec_subsec.model_section_id = '" + subsectionRow.parent_section_id  + "'");

        let nextSecIdResult = await clientOld.query(
          "SELECT subsections_id " +
        	"FROM model_sections_subsections sec_subsec " +
          "WHERE sec_subsec.subsections_order = " + ixOfSubsec + " " +
        	"AND sec_subsec.model_section_id = '" + subsectionRow.parent_section_id  + "'");

        let thisSecId = thisSecIdResult.rows[0].subsections_id;
        let nextSecId = nextSecIdResult.rows[0].subsections_id;
        console.log('thisSecId: ' + thisSecId)
        console.log('nextSecId: ' + nextSecId)

        let thisSubsecResult = await clientNew.query(
          "SELECT id FROM model_subsections " +
          "WHERE parent_section_id = '" + subsectionRow.parent_section_id + "' " +
          "AND section_id = '" + thisSecId + "'")

        let nextSubsecResult = await clientNew.query(
          "SELECT id FROM model_subsections " +
          "WHERE parent_section_id = '" + subsectionRow.parent_section_id + "' " +
          "AND section_id = '" + nextSecId + "'")

        let thisSubsecId = thisSubsecResult.rows[0].id
        let nextSubsecId = nextSubsecResult.rows[0].id

        await clientNew.query(
          "UPDATE model_subsections subsection " +
          "SET before_element_id = '" + nextSubsecId + "'" +
          "WHERE subsection.id = '" + thisSubsecId + "'");

        await clientNew.query(
          "UPDATE model_subsections subsection " +
          "SET after_element_id = '" + thisSubsecId + "'" +
          "WHERE subsection.id = '" + nextSubsecId + "'");
      }
    }
  }

  process.exit()

})();
