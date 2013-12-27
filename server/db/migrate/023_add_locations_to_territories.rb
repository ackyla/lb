class AddLocationsToTerritories < ActiveRecord::Migration
  def self.up
    change_table :territories do |t|
      t.references :locations
    end
  end

  def self.down
    change_table :territories do |t|
      t.remove :locations
    end
  end
end
