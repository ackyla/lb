class AddRadiusToHitLocation < ActiveRecord::Migration
  def self.up
    change_table :hitlocations do |t|
      t.float :radius
    end
  end

  def self.down
    change_table :hitlocations do |t|
      t.remove :radius
    end
  end
end
