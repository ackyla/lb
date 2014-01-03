class CreateLocationsAndTerritoriesTable < ActiveRecord::Migration
  def self.up
    create_table :locations_territories, index: false do |t|
      t.references :location
      t.references :territory
    end
  end

  def self.down
    remove_table :locations_territories
  end
end
