var pg = require('pg');

(async function(){
  var clientOld = new pg.Client("postgres://postgres:iristra@localhost:5432/c1db-bk");
  clientOld.connect();

  var clientNew = new pg.Client("postgres://postgres:iristra@localhost:5432/c1db");
  clientNew.connect();

  /* get all sections in new DB */
  const sectionsResult = await clientNew.query("SELECT id FROM model_sections");
  
  for (var ixInSections = 0; ixInSections < sectionsResult.rows.length; ixInSections++) {
    let row = sectionsResult.rows[ixInSections]
    console.log('section id: ' + row.id)
    /* get the number of card additions in old DB */
    var maxOrdersResult = await clientOld.query(
      "SELECT model_section_id, MAX(cards_order) " +
      "FROM model_sections_cards_wrappers_additions_common " +
      "WHERE model_section_id = '" + row.id + "' " +
      "GROUP BY model_section_id");

    if (maxOrdersResult.rows.length > 0) {
      /* update the before and after reference if more than one card */
      let nCards = maxOrdersResult.rows[0].max + 1
      console.log('nCards: ' + nCards)

      for (var ixOfCard = 1; ixOfCard < nCards; ixOfCard++) {

        console.log('updating card ' + (ixOfCard + 1))

        let thisCardIdResult = await clientOld.query(
          "SELECT cards_wrappers_additions_common_id " +
          "FROM model_sections_cards_wrappers_additions_common crd_add_com " +
          "WHERE crd_add_com.cards_order = " + (ixOfCard - 1) + " " +
          "AND crd_add_com.model_section_id = '" + row.id  + "'");

        let nextCardIdResult = await clientOld.query(
          "SELECT cards_wrappers_additions_common_id " +
        	"FROM model_sections_cards_wrappers_additions_common crd_add_com " +
          "WHERE crd_add_com.cards_order = " + ixOfCard + " " +
        	"AND crd_add_com.model_section_id = '" + row.id  + "'");

        let thisCardId = thisCardIdResult.rows[0].cards_wrappers_additions_common_id;
        let nextCardId = nextCardIdResult.rows[0].cards_wrappers_additions_common_id;
        console.log('thisCardId: ' + thisCardId)
        console.log('nextCardId: ' + nextCardId)

        await clientNew.query(
          "UPDATE model_cards_wrapper_additions crd_add " +
          "SET before_card_wrapper_addition_id = '" + nextCardId + "'" +
          "WHERE crd_add.id = '" + thisCardId + "'");

        await clientNew.query(
          "UPDATE model_cards_wrapper_additions crd_add " +
          "SET after_card_wrapper_addition_id = '" + thisCardId + "'" +
          "WHERE crd_add.id = '" + nextCardId + "'");

      }
    }
  }
})();


// query.on('row', function(row) {
//     console.log(row);
// });
