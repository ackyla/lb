class CreateCoordinates < ActiveRecord::Migration
  def self.up
    create_table :coordinates do |t|
      t.float :lat
      t.float :long
      t.timestamps
    end

    add_column :locations, :coordinate_id, :integer
    add_column :territories, :coordinate_id, :integer

    (Location.all.to_a + Territory.all.to_a).each{|e|
      params = {lat: e.latitude, long: e.longitude}
      c = (Coordinate.where(params).first || Coordinate.create(params))
      e.coordinate = c
      e.save
    }

    remove_column :locations, :latitude, :longitude
    remove_column :territories, :latitude, :longitude
  end

  def self.down
    drop_table :coordinates
    remove_column :locations, :coordinate_id
    remove_column :territories, :coordinate_id
    # add_colmn :locations, [:latitude, :longitude]
    # add_colmn :territories, [:latitude, :longitude]
  end
end
