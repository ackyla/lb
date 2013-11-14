class CreateTerritories < ActiveRecord::Migration
  def self.up
    create_table :territories do |t|
      t.float :latitude
      t.float :longitude
      t.float :radius
      t.integer :user_id
      t.timestamps
    end
  end

  def self.down
    drop_table :territories
  end
end
